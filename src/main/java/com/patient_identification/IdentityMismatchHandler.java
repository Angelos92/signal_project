package com.patient_identification;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles identity mismatches and stores anomaly records.
 */
public class IdentityMismatchHandler {
    private List<IdentityAnomaly> anomalies;

    public IdentityMismatchHandler() {
        this.anomalies = new ArrayList<>();
    }

    public IdentityAnomaly handleUnknownPatient(int simulatorPatientId) {
        IdentityAnomaly anomaly = new IdentityAnomaly(
                simulatorPatientId,
                "No matching hospital patient found",
                System.currentTimeMillis()
        );

        anomalies.add(anomaly);
        return anomaly;
    }

    public List<IdentityAnomaly> getAnomalies() {
        return new ArrayList<>(anomalies);
    }
}