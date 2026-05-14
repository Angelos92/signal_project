package com.data_management;

import com.data_management.websocket.WebSocketDataClient;

import java.io.IOException;
import java.net.URI;

/**
 * DataReader implementation that connects to a WebSocket server
 * and continuously receives patient data.
 */
public class WebSocketDataReader implements DataReader {
    private URI serverUri;
    private WebSocketDataClient client;

    public WebSocketDataReader(String serverUrl) {
        this.serverUri = URI.create(serverUrl);
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        this.client = new WebSocketDataClient(serverUri, dataStorage);
        client.connect();
    }

    public WebSocketDataClient getClient() {
        return client;
    }
}