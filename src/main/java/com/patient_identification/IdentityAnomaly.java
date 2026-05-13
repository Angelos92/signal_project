package com.patient_identification;

/**
 * Represents an identity-related anomaly, such as an unknown patient ID.
 */
public class IdentityAnomaly {
    private int simulatorPatientId;
    private String reason;
    private long timestamp;

    public IdentityAnomaly(int simulatorPatientId, String reason, long timestamp) {
        this.simulatorPatientId = simulatorPatientId;
        this.reason = reason;
        this.timestamp = timestamp;
    }

    public int getSimulatorPatientId() {
        return simulatorPatientId;
    }

    public String getReason() {
        return reason;
    }

    public long getTimestamp() {
        return timestamp;
    }
}