package com.alerts;

import com.alerts.strategy.AlertStrategy;
import com.alerts.strategy.BloodPressureStrategy;
import com.alerts.strategy.ECGStrategy;
import com.alerts.strategy.ManualAlertStrategy;
import com.alerts.strategy.OxygenSaturationStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class AlertGenerator {
    private static final long RECENT_TIME_WINDOW_MS = 10 * 60 * 1000;

    private DataStorage dataStorage;
    private AlertManager alertManager;
    private List<AlertStrategy> alertStrategies;

    public AlertGenerator(DataStorage dataStorage, AlertManager alertManager) {
        this.dataStorage = dataStorage;
        this.alertManager = alertManager;
        this.alertStrategies = new ArrayList<>();

        alertStrategies.add(new BloodPressureStrategy());
        alertStrategies.add(new OxygenSaturationStrategy());
        alertStrategies.add(new ECGStrategy());
        alertStrategies.add(new ManualAlertStrategy());
    }

    public void evaluateData(Patient patient) {
        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - RECENT_TIME_WINDOW_MS;

        List<PatientRecord> recentRecords = patient.getRecords(startTime, currentTime);

        for (AlertStrategy strategy : alertStrategies) {
            List<Alert> alerts = strategy.checkAlert(recentRecords);

            for (Alert alert : alerts) {
                alertManager.dispatchAlert(alert);
            }
        }
    }
}