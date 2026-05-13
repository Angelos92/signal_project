package com.alerts.strategy;

import com.alerts.Alert;
import com.alerts.factory.ManualAlertFactory;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class ManualAlertStrategy implements AlertStrategy {
    private final ManualAlertFactory alertFactory;

    public ManualAlertStrategy() {
        this.alertFactory = new ManualAlertFactory();
    }

    @Override
    public List<Alert> checkAlert(List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();

        for (PatientRecord record : records) {
            if (record.getRecordType().equalsIgnoreCase("Alert")
                    && record.getMeasurementValue() == 1.0) {
                alerts.add(alertFactory.createAlert(
                        String.valueOf(record.getPatientId()),
                        "Manual alert triggered",
                        record.getTimestamp()
                ));
            }
        }

        return alerts;
    }
}