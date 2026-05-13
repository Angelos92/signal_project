package com.data_access;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Receives raw patient data from a log file.
 */
public class FileDataListener implements DataListener {
    private String filePath;
    private BufferedReader reader;

    public FileDataListener(String filePath) throws IOException {
        this.filePath = filePath;
        this.reader = new BufferedReader(new FileReader(filePath));
    }

    @Override
    public RawDataMessage listen() throws IOException {
        String line = reader.readLine();

        if (line == null) {
            return null;
        }

        return new RawDataMessage(
                "FILE",
                line,
                System.currentTimeMillis()
        );
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    public String getFilePath() {
        return filePath;
    }
}