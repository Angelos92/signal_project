package com.data_access;

import com.data_management.PatientRecord;

/**
 * Converts raw data messages into standardized PatientRecord objects.
 */
public class DataParser {

    /**
     * Checks whether the raw message can be parsed.
     *
     * @param rawDataMessage the raw data message
     * @return true if the message is valid, false otherwise
     */
    public boolean isValid(RawDataMessage rawDataMessage) {
        if (rawDataMessage == null || rawDataMessage.getRawContent() == null) {
            return false;
        }

        String[] parts = rawDataMessage.getRawContent().split(",");
        return parts.length == 4;
    }

    /**
     * Parses a raw data message into a PatientRecord.
     *
     * Expected format:
     * patientId,recordType,measurementValue,timestamp
     *
     * @param rawDataMessage the raw data message
     * @return a PatientRecord object
     * @throws IllegalArgumentException if the message format is invalid
     */
    public PatientRecord parse(RawDataMessage rawDataMessage) {
        if (!isValid(rawDataMessage)) {
            throw new IllegalArgumentException("Invalid raw data message format.");
        }

        String[] parts = rawDataMessage.getRawContent().split(",");

        int patientId = Integer.parseInt(parts[0].trim());
        String recordType = parts[1].trim();
        double measurementValue = Double.parseDouble(parts[2].trim());
        long timestamp = Long.parseLong(parts[3].trim());

        return new PatientRecord(patientId, measurementValue, recordType, timestamp);
    }
}