package com.alerts.factory;

import com.alerts.Alert;
import com.alerts.ManualAlert;

public class ManualAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new ManualAlert(patientId, condition, timestamp);
    }
}