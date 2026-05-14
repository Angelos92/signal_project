package com.data_management;

/**
 * Parses patient data messages from file output or WebSocket output.
 *
 * Supported formats:
 * 1. File format:
 *    Patient ID: 29, Timestamp: 1778690404972, Label: Saturation, Data: 99.0%
 *
 * 2. WebSocket format:
 *    29,1778690404972,Saturation,99.0%
 */
public class PatientDataParser {

    public PatientRecord parse(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or blank.");
        }

        String[] parts = message.split(",");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid message format: " + message);
        }

        int patientId = parsePatientId(parts[0]);
        long timestamp = parseTimestamp(parts[1]);
        String recordType = parseRecordType(parts[2]);
        double measurementValue = parseMeasurementValue(parts[3]);

        return new PatientRecord(patientId, measurementValue, recordType, timestamp);
    }

    private int parsePatientId(String part) {
        return Integer.parseInt(part.replace("Patient ID:", "").trim());
    }

    private long parseTimestamp(String part) {
        return Long.parseLong(part.replace("Timestamp:", "").trim());
    }

    private String parseRecordType(String part) {
        String label = part.replace("Label:", "").trim();

        if (label.equalsIgnoreCase("Saturation")) {
            return "BloodSaturation";
        }

        return label;
    }

    private double parseMeasurementValue(String part) {
        String value = part.replace("Data:", "")
                .replace("%", "")
                .trim();

        return Double.parseDouble(value);
    }
}