package com.data_access;

/**
 * Represents unprocessed data received from an external data source.
 */
public class RawDataMessage {
    private String sourceType;
    private String rawContent;
    private long receivedTimestamp;

    public RawDataMessage(String sourceType, String rawContent, long receivedTimestamp) {
        this.sourceType = sourceType;
        this.rawContent = rawContent;
        this.receivedTimestamp = receivedTimestamp;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getRawContent() {
        return rawContent;
    }

    public long getReceivedTimestamp() {
        return receivedTimestamp;
    }
}