package com.data_access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Receives raw patient data over a TCP connection.
 */
public class TCPDataListener implements DataListener {
    private int port;
    private ServerSocket serverSocket;

    public TCPDataListener(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public RawDataMessage listen() throws IOException {
        Socket clientSocket = serverSocket.accept();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()))) {

            String line = reader.readLine();

            return new RawDataMessage(
                    "TCP",
                    line,
                    System.currentTimeMillis()
            );
        } finally {
            clientSocket.close();
        }
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }

    public int getPort() {
        return port;
    }
}