package com.data_access;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Receives raw patient data from a WebSocket source.
 * This simplified version stores received messages in a queue.
 */
public class WebSocketDataListener implements DataListener {
    private String url;
    private Queue<String> messageQueue;

    public WebSocketDataListener(String url) {
        this.url = url;
        this.messageQueue = new LinkedList<>();
    }

    /**
     * Adds a message to the queue.
     * In a real WebSocket implementation, this would be called when a message arrives.
     *
     * @param message the raw WebSocket message
     */
    public void receiveMessage(String message) {
        messageQueue.add(message);
    }

    @Override
    public RawDataMessage listen() throws IOException {
        String message = messageQueue.poll();

        if (message == null) {
            return null;
        }

        return new RawDataMessage(
                "WEBSOCKET",
                message,
                System.currentTimeMillis()
        );
    }

    @Override
    public void close() throws IOException {
        messageQueue.clear();
    }

    public String getUrl() {
        return url;
    }
}