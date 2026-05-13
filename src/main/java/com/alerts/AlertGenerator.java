package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class AlertGenerator {
    private static final long RECENT_TIME_WINDOW_MS = 5 * 60 * 1000;

    private static final double MAX_HEART_RATE = 130.0;
    private static final double MIN_OXYGEN_SATURATION = 90.0;
    private static final double MAX_BLOOD_PRESSURE = 140.0;

    private DataStorage dataStorage;
    private AlertManager alertManager;

    public AlertGenerator(DataStorage dataStorage, AlertManager alertManager) {
        this.dataStorage = dataStorage;
        this.alertManager = alertManager;
    }

    public void evaluateData(Patient patient) {
        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - RECENT_TIME_WINDOW_MS;

        List<PatientRecord> recentRecords = patient.getRecords(startTime, currentTime);

        for (PatientRecord record : recentRecords) {
            Alert alert = createAlertIfCritical(record);

            if (alert != null) {
                alertManager.dispatchAlert(alert);
            }
        }
    }

    private Alert createAlertIfCritical(PatientRecord record) {
        String recordType = record.getRecordType();
        double value = record.getMeasurementValue();

        if ((recordType.equalsIgnoreCase("HeartRate")
                || recordType.equalsIgnoreCase("ECG"))
                && value > MAX_HEART_RATE) {

            return new Alert(
                    String.valueOf(record.getPatientId()),
                    "High heart rate: " + value,
                    record.getTimestamp()
            );
        }

        if (recordType.equalsIgnoreCase("BloodSaturation")
                && value < MIN_OXYGEN_SATURATION) {

            return new Alert(
                    String.valueOf(record.getPatientId()),
                    "Low oxygen saturation: " + value,
                    record.getTimestamp()
            );
        }

        if (recordType.equalsIgnoreCase("BloodPressure")
                && value > MAX_BLOOD_PRESSURE) {

            return new Alert(
                    String.valueOf(record.getPatientId()),
                    "High blood pressure: " + value,
                    record.getTimestamp()
            );
        }

        return null;
    }
}