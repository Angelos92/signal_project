package com.alerts;

public class BloodOxygenAlert extends BasicAlert {
    public BloodOxygenAlert(String patientId, String condition, long timestamp) {
        super(patientId, "Blood Oxygen Alert: " + condition, timestamp);
    }
}