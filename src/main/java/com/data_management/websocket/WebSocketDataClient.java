package com.data_management.websocket;

import com.data_management.DataStorage;
import com.data_management.PatientDataParser;
import com.data_management.PatientRecord;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * WebSocket client that receives real-time patient data messages,
 * parses them, and stores them in DataStorage.
 */
public class WebSocketDataClient extends WebSocketClient {
    private DataStorage dataStorage;
    private PatientDataParser parser;
    private boolean connected;
    private String lastErrorMessage;

    public WebSocketDataClient(URI serverUri, DataStorage dataStorage) {
        super(serverUri);

        // Store a reference to the shared DataStorage 
        this.dataStorage = dataStorage;

        // Reuse the parser so WebSocket messages are converted into the same PatientRecord format used by the rest of the system.
        this.parser = new PatientDataParser();

        // Track connection state for testing and error handling.
        this.connected = false;
        this.lastErrorMessage = null;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        // Mark the client as connected once the WebSocket handshake succeeds.
        connected = true;
        System.out.println("Connected to WebSocket server.");
    }

    @Override
    public void onMessage(String message) {
        // Every message received from the WebSocket server is processed immediately so patient data is stored in real time. 
        processMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // If the connection closes, mark the client as disconnected.
        connected = false;
        System.out.println("WebSocket connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        // Store the latest error 
        lastErrorMessage = ex.getMessage();
        System.err.println("WebSocket error: " + ex.getMessage());
    }

    /**
     * Processes one raw WebSocket message.
     * This method is public so it can be tested without needing a live server.
     *
     * @param message the raw message received from the WebSocket server
     */
    public void processMessage(String message) {
        try {
            // Parse the raw WebSocket message into a standard PatientRecord.
            // Expected WebSocket format: patientId,timestamp,label,data
            PatientRecord record = parser.parse(message);

            // Store the parsed patient data using the existing storage method.
            dataStorage.addPatientData(
                    record.getPatientId(),
                    record.getMeasurementValue(),
                    record.getRecordType(),
                    record.getTimestamp()
            );
        } catch (IllegalArgumentException e) {
            // If a message is corrupted, incomplete, or cannot be parsed, skip it instead of stopping the real-time data stream.
            lastErrorMessage = "Invalid message skipped: " + message;
            System.err.println(lastErrorMessage);
        }
    }

    public boolean isConnectedToServer() {
        return connected;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }
}