package com;

import com.alerts.AlertGenerator;
import com.alerts.AlertManager;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class Main {
    public static void main(String[] args) {
        DataStorage storage = new DataStorage();
        AlertManager alertManager = new AlertManager();
        AlertGenerator alertGenerator = new AlertGenerator(storage, alertManager);

        storage.addPatientData(1, 145.0, "HeartRate", System.currentTimeMillis());
        storage.addPatientData(1, 88.0, "BloodSaturation", System.currentTimeMillis());

        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }
}