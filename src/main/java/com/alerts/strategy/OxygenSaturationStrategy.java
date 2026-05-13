package com.alerts.strategy;

import com.alerts.Alert;
import com.alerts.factory.BloodOxygenAlertFactory;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {
    private static final long RAPID_DROP_WINDOW_MS = 10 * 60 * 1000;
    private static final double MIN_OXYGEN_SATURATION = 92.0;
    private static final double RAPID_SATURATION_DROP = 5.0;
    private static final double MIN_SYSTOLIC_PRESSURE = 90.0;

    private final BloodOxygenAlertFactory alertFactory;

    public OxygenSaturationStrategy() {
        this.alertFactory = new BloodOxygenAlertFactory();
    }

    @Override
    public List<Alert> checkAlert(List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();

        alerts.addAll(checkLowSaturation(records));
        alerts.addAll(checkRapidOxygenDrop(records));
        alerts.addAll(checkHypotensiveHypoxemia(records));

        return alerts;
    }

    private List<Alert> checkLowSaturation(List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();

        for (PatientRecord record : records) {
            if (isSaturationRecord(record.getRecordType())
                    && record.getMeasurementValue() < MIN_OXYGEN_SATURATION) {
                alerts.add(alertFactory.createAlert(
                        String.valueOf(record.getPatientId()),
                        "Low oxygen saturation: " + record.getMeasurementValue(),
                        record.getTimestamp()
                ));
            }
        }

        return alerts;
    }

    private List<Alert> checkRapidOxygenDrop(List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();
        List<PatientRecord> saturationRecords = new ArrayList<>();

        for (PatientRecord record : records) {
            if (isSaturationRecord(record.getRecordType())) {
                saturationRecords.add(record);
            }
        }

        if (saturationRecords.size() < 2) {
            return alerts;
        }

        saturationRecords.sort(Comparator.comparingLong(PatientRecord::getTimestamp));

        for (int i = 0; i < saturationRecords.size(); i++) {
            for (int j = i + 1; j < saturationRecords.size(); j++) {
                PatientRecord earlier = saturationRecords.get(i);
                PatientRecord later = saturationRecords.get(j);

                long timeDifference = later.getTimestamp() - earlier.getTimestamp();

                if (timeDifference > RAPID_DROP_WINDOW_MS) {
                    break;
                }

                double drop = earlier.getMeasurementValue() - later.getMeasurementValue();

                if (drop >= RAPID_SATURATION_DROP) {
                    alerts.add(alertFactory.createAlert(
                            String.valueOf(later.getPatientId()),
                            "Rapid oxygen saturation drop: " + drop + "%",
                            later.getTimestamp()
                    ));
                    return alerts;
                }
            }
        }

        return alerts;
    }

    private List<Alert> checkHypotensiveHypoxemia(List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();

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

            alerts.add(alertFactory.createAlert(
                    String.valueOf(lowSystolicRecord.getPatientId()),
                    "Hypotensive hypoxemia alert",
                    alertTimestamp
            ));
        }

        return alerts;
    }

    private boolean isSaturationRecord(String recordType) {
        return recordType.equalsIgnoreCase("BloodSaturation")
                || recordType.equalsIgnoreCase("Saturation");
    }
}