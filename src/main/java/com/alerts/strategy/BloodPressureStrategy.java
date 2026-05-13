package com.alerts.strategy;

import com.alerts.Alert;
import com.alerts.factory.BloodPressureAlertFactory;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {
    private static final double MAX_SYSTOLIC_PRESSURE = 180.0;
    private static final double MIN_SYSTOLIC_PRESSURE = 90.0;
    private static final double MAX_DIASTOLIC_PRESSURE = 120.0;
    private static final double MIN_DIASTOLIC_PRESSURE = 60.0;
    private static final double TREND_CHANGE = 10.0;

    private final BloodPressureAlertFactory alertFactory;

    public BloodPressureStrategy() {
        this.alertFactory = new BloodPressureAlertFactory();
    }

    @Override
    public List<Alert> checkAlert(List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();

        for (PatientRecord record : records) {
            String type = record.getRecordType();
            double value = record.getMeasurementValue();

            if (type.equalsIgnoreCase("SystolicPressure")
                    && (value > MAX_SYSTOLIC_PRESSURE || value < MIN_SYSTOLIC_PRESSURE)) {
                alerts.add(alertFactory.createAlert(
                        String.valueOf(record.getPatientId()),
                        "Critical systolic blood pressure: " + value,
                        record.getTimestamp()
                ));
            }

            if (type.equalsIgnoreCase("DiastolicPressure")
                    && (value > MAX_DIASTOLIC_PRESSURE || value < MIN_DIASTOLIC_PRESSURE)) {
                alerts.add(alertFactory.createAlert(
                        String.valueOf(record.getPatientId()),
                        "Critical diastolic blood pressure: " + value,
                        record.getTimestamp()
                ));
            }
        }

        alerts.addAll(checkTrend(records, "SystolicPressure"));
        alerts.addAll(checkTrend(records, "DiastolicPressure"));

        return alerts;
    }

    private List<Alert> checkTrend(List<PatientRecord> records, String pressureType) {
        List<Alert> alerts = new ArrayList<>();
        List<PatientRecord> pressureRecords = filterRecordsByType(records, pressureType);

        if (pressureRecords.size() < 3) {
            return alerts;
        }

        pressureRecords.sort(Comparator.comparingLong(PatientRecord::getTimestamp));

        for (int i = 0; i <= pressureRecords.size() - 3; i++) {
            PatientRecord first = pressureRecords.get(i);
            PatientRecord second = pressureRecords.get(i + 1);
            PatientRecord third = pressureRecords.get(i + 2);

            double firstValue = first.getMeasurementValue();
            double secondValue = second.getMeasurementValue();
            double thirdValue = third.getMeasurementValue();

            boolean increasing = secondValue - firstValue > TREND_CHANGE
                    && thirdValue - secondValue > TREND_CHANGE;

            boolean decreasing = firstValue - secondValue > TREND_CHANGE
                    && secondValue - thirdValue > TREND_CHANGE;

            if (increasing) {
                alerts.add(alertFactory.createAlert(
                        String.valueOf(third.getPatientId()),
                        pressureType + " increasing trend detected",
                        third.getTimestamp()
                ));
            }

            if (decreasing) {
                alerts.add(alertFactory.createAlert(
                        String.valueOf(third.getPatientId()),
                        pressureType + " decreasing trend detected",
                        third.getTimestamp()
                ));
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