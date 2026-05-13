package com.alerts.strategy;

import com.alerts.Alert;
import com.alerts.factory.ECGAlertFactory;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class ECGStrategy implements AlertStrategy {
    private static final double ECG_PEAK_DIFFERENCE = 1.0;

    private final ECGAlertFactory alertFactory;

    public ECGStrategy() {
        this.alertFactory = new ECGAlertFactory();
    }

    @Override
    public List<Alert> checkAlert(List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();
        List<PatientRecord> ecgRecords = filterRecordsByType(records, "ECG");

        if (ecgRecords.size() < 3) {
            return alerts;
        }

        double total = 0.0;

        for (PatientRecord record : ecgRecords) {
            total += record.getMeasurementValue();
        }

        double average = total / ecgRecords.size();

        for (PatientRecord record : ecgRecords) {
            double value = record.getMeasurementValue();

            if (Math.abs(value - average) > ECG_PEAK_DIFFERENCE) {
                alerts.add(alertFactory.createAlert(
                        String.valueOf(record.getPatientId()),
                        "Abnormal ECG peak detected: " + value,
                        record.getTimestamp()
                ));
                return alerts;
            }
        }

        return alerts;
    }

    private List<PatientRecord> filterRecordsByType(List<PatientRecord> records, String recordType) {
        List<PatientRecord> filteredRecords = new ArrayList<>();

        for (PatientRecord record : records) {
            if (record.getRecordType().equalsIgnoreCase(recordType)) {
                filteredRecords.add(record);
            }
        }

        return filteredRecords;
    }
}