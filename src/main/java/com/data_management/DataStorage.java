package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStorage {
    private static DataStorage instance;

    private Map<Integer, Patient> patientMap;

    private DataStorage() {
        this.patientMap = new HashMap<>();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public void clear() {
        patientMap.clear();
    }

    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);

        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }

        patient.addRecord(measurementValue, recordType, timestamp);
    }

    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);

        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }

        return new ArrayList<>();
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    public Patient getPatient(int patientId) {
        return patientMap.get(patientId);
    }

    public void applyDeletionPolicy(DeletionPolicy deletionPolicy) {
        long currentTime = System.currentTimeMillis();

        for (Patient patient : patientMap.values()) {
            patient.removeOldRecords(deletionPolicy, currentTime);
        }
    }
}