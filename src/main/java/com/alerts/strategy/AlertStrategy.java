package com.alerts.strategy;

import com.alerts.Alert;
import com.data_management.PatientRecord;

import java.util.List;

public interface AlertStrategy {
    List<Alert> checkAlert(List<PatientRecord> records);
}
