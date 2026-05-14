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
        this.dataStorage = dataStorage;
        this.parser = new PatientDataParser();
        this.connected = false;
        this.lastErrorMessage = null;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        connected = true;
        System.out.println("Connected to WebSocket server.");
    }

    @Override
    public void onMessage(String message) {
        processMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        connected = false;
        System.out.println("WebSocket connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
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
            PatientRecord record = parser.parse(message);

            dataStorage.addPatientData(
                    record.getPatientId(),
                    record.getMeasurementValue(),
                    record.getRecordType(),
                    record.getTimestamp()
            );
        } catch (IllegalArgumentException e) {
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