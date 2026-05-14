package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStorage {
    private static DataStorage instance;

    // Stores all patients by their patient ID.
    // Each Patient object then manages its own list of PatientRecord objects.
    private Map<Integer, Patient> patientMap;

    private DataStorage() {
        this.patientMap = new HashMap<>();
    }

    public static synchronized DataStorage getInstance() {
        // Singleton pattern: only one DataStorage instance should exist.
        //  Data and other system components should all update the same storage object.
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public synchronized void clear() {
        // Used mainly by tests to reset the singleton storage between test cases.
        patientMap.clear();
    }

    public synchronized void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        // Look up the patient by ID. If this is the first record for the patient, create a new Patient object and add it to the storage map.
        Patient patient = patientMap.get(patientId);

        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }

        // Append the new real-time or file-based measurement to the patient's records.
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    public synchronized List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        // Retrieve the patient first, then let the Patient class filter its records
        // by timestamp. This keeps patient-specific record filtering inside Patient.
        Patient patient = patientMap.get(patientId);

        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }

        // Return an empty list instead of null
        return new ArrayList<>();
    }

    public synchronized List<Patient> getAllPatients() {
        // Return a copy of the patients list so external classes cannot directly modify the internal map structure.
        return new ArrayList<>(patientMap.values());
    }

    public synchronized Patient getPatient(int patientId) {
        // Returns the stored Patient object for systems such as alert generation.
        return patientMap.get(patientId);
    }

    public synchronized void applyDeletionPolicy(DeletionPolicy deletionPolicy) {
        long currentTime = System.currentTimeMillis();

        // Apply the retention rule to every patient. Each Patient removes records that the DeletionPolicy marks as too old.
        for (Patient patient : patientMap.values()) {
            patient.removeOldRecords(deletionPolicy, currentTime);
        }
    }
}