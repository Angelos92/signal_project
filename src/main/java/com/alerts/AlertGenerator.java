package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AlertGenerator {
    private static final long RECENT_TIME_WINDOW_MS = 10 * 60 * 1000; // 10 minutes

    private static final double MIN_OXYGEN_SATURATION = 92.0;
    private static final double RAPID_SATURATION_DROP = 5.0;

    private static final double MAX_SYSTOLIC_PRESSURE = 180.0;
    private static final double MIN_SYSTOLIC_PRESSURE = 90.0;
    private static final double MAX_DIASTOLIC_PRESSURE = 120.0;
    private static final double MIN_DIASTOLIC_PRESSURE = 60.0;

    private static final double BLOOD_PRESSURE_TREND_CHANGE = 10.0;

    private static final double ECG_PEAK_DIFFERENCE = 1.0;

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

        checkIndividualCriticalAlerts(recentRecords);
        checkBloodPressureTrend(recentRecords, "SystolicPressure");
        checkBloodPressureTrend(recentRecords, "DiastolicPressure");
        checkRapidOxygenDrop(recentRecords);
        checkHypotensiveHypoxemia(recentRecords);
        checkAbnormalECG(recentRecords);
    }

    private void checkIndividualCriticalAlerts(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            Alert alert = createAlertIfCritical(record);

            if (alert != null) {
                alertManager.dispatchAlert(alert);
            }
        }
    }

    private Alert createAlertIfCritical(PatientRecord record) {
        String recordType = record.getRecordType();
        double value = record.getMeasurementValue();

        if (recordType.equalsIgnoreCase("SystolicPressure")
                && (value > MAX_SYSTOLIC_PRESSURE || value < MIN_SYSTOLIC_PRESSURE)) {

            return new Alert(
                    String.valueOf(record.getPatientId()),
                    "Critical systolic blood pressure: " + value,
                    record.getTimestamp()
            );
        }

        if (recordType.equalsIgnoreCase("DiastolicPressure")
                && (value > MAX_DIASTOLIC_PRESSURE || value < MIN_DIASTOLIC_PRESSURE)) {

            return new Alert(
                    String.valueOf(record.getPatientId()),
                    "Critical diastolic blood pressure: " + value,
                    record.getTimestamp()
            );
        }

        if (isSaturationRecord(recordType) && value < MIN_OXYGEN_SATURATION) {
            return new Alert(
                    String.valueOf(record.getPatientId()),
                    "Low oxygen saturation: " + value,
                    record.getTimestamp()
            );
        }

        if (recordType.equalsIgnoreCase("Alert") && value == 1.0) {
            return new Alert(
                    String.valueOf(record.getPatientId()),
                    "Manual alert triggered",
                    record.getTimestamp()
            );
        }

        return null;
    }

    private void checkBloodPressureTrend(List<PatientRecord> records, String pressureType) {
        List<PatientRecord> pressureRecords = filterRecordsByType(records, pressureType);

        if (pressureRecords.size() < 3) {
            return;
        }

        pressureRecords.sort(Comparator.comparingLong(PatientRecord::getTimestamp));

        for (int i = 0; i <= pressureRecords.size() - 3; i++) {
            PatientRecord first = pressureRecords.get(i);
            PatientRecord second = pressureRecords.get(i + 1);
            PatientRecord third = pressureRecords.get(i + 2);

            double firstValue = first.getMeasurementValue();
            double secondValue = second.getMeasurementValue();
            double thirdValue = third.getMeasurementValue();

            boolean increasing = secondValue - firstValue > BLOOD_PRESSURE_TREND_CHANGE
                    && thirdValue - secondValue > BLOOD_PRESSURE_TREND_CHANGE;

            boolean decreasing = firstValue - secondValue > BLOOD_PRESSURE_TREND_CHANGE
                    && secondValue - thirdValue > BLOOD_PRESSURE_TREND_CHANGE;

            if (increasing) {
                alertManager.dispatchAlert(new Alert(
                        String.valueOf(third.getPatientId()),
                        pressureType + " increasing trend detected",
                        third.getTimestamp()
                ));
            }

            if (decreasing) {
                alertManager.dispatchAlert(new Alert(
                        String.valueOf(third.getPatientId()),
                        pressureType + " decreasing trend detected",
                        third.getTimestamp()
                ));
            }
        }
    }

    private void checkRapidOxygenDrop(List<PatientRecord> records) {
        List<PatientRecord> saturationRecords = new ArrayList<>();

        for (PatientRecord record : records) {
            if (isSaturationRecord(record.getRecordType())) {
                saturationRecords.add(record);
            }
        }

        if (saturationRecords.size() < 2) {
            return;
        }

        saturationRecords.sort(Comparator.comparingLong(PatientRecord::getTimestamp));

        for (int i = 0; i < saturationRecords.size(); i++) {
            for (int j = i + 1; j < saturationRecords.size(); j++) {
                PatientRecord earlier = saturationRecords.get(i);
                PatientRecord later = saturationRecords.get(j);

                long timeDifference = later.getTimestamp() - earlier.getTimestamp();

                if (timeDifference > RECENT_TIME_WINDOW_MS) {
                    break;
                }

                double drop = earlier.getMeasurementValue() - later.getMeasurementValue();

                if (drop >= RAPID_SATURATION_DROP) {
                    alertManager.dispatchAlert(new Alert(
                            String.valueOf(later.getPatientId()),
                            "Rapid oxygen saturation drop: " + drop + "%",
                            later.getTimestamp()
                    ));
                    return;
                }
            }
        }
    }

    private void checkHypotensiveHypoxemia(List<PatientRecord> records) {
        PatientRecord lowSystolicRecord = null;
        PatientRecord lowSaturationRecord = null;

        for (PatientRecord record : records) {
            String recordType = record.getRecordType();
            double value = record.getMeasurementValue();

            if (recordType.equalsIgnoreCase("SystolicPressure") && value < MIN_SYSTOLIC_PRESSURE) {
                lowSystolicRecord = record;
            }

            if (isSaturationRecord(recordType) && value < MIN_OXYGEN_SATURATION) {
                lowSaturationRecord = record;
            }
        }

        if (lowSystolicRecord != null && lowSaturationRecord != null) {
            long alertTimestamp = Math.max(
                    lowSystolicRecord.getTimestamp(),
                    lowSaturationRecord.getTimestamp()
            );

            alertManager.dispatchAlert(new Alert(
                    String.valueOf(lowSystolicRecord.getPatientId()),
                    "Hypotensive hypoxemia alert",
                    alertTimestamp
            ));
        }
    }

    private void checkAbnormalECG(List<PatientRecord> records) {
        List<PatientRecord> ecgRecords = filterRecordsByType(records, "ECG");

        if (ecgRecords.size() < 3) {
            return;
        }

        double total = 0.0;

        for (PatientRecord record : ecgRecords) {
            total += record.getMeasurementValue();
        }

        double average = total / ecgRecords.size();

        for (PatientRecord record : ecgRecords) {
            double value = record.getMeasurementValue();

            if (Math.abs(value - average) > ECG_PEAK_DIFFERENCE) {
                alertManager.dispatchAlert(new Alert(
                        String.valueOf(record.getPatientId()),
                        "Abnormal ECG peak detected: " + value,
                        record.getTimestamp()
                ));
                return;
            }
        }
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

    private boolean isSaturationRecord(String recordType) {
        return recordType.equalsIgnoreCase("BloodSaturation")
                || recordType.equalsIgnoreCase("Saturation");
    }
}