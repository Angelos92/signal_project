package com;

import com.data_management.AccessController;
import com.data_management.AuditLog;
import com.data_management.DataRetriever;
import com.data_management.DataStorage;
import com.data_management.DeletionPolicy;
import com.data_management.PatientRecord;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 145.0, "HeartRate", System.currentTimeMillis());
        storage.addPatientData(1, 88.0, "BloodSaturation", System.currentTimeMillis());

        AccessController accessController = new AccessController();
        AuditLog auditLog = new AuditLog();
        DataRetriever retriever = new DataRetriever(storage, accessController, auditLog);

        List<PatientRecord> records = retriever.getPatientRecords(
                1,
                System.currentTimeMillis() - 10_000,
                System.currentTimeMillis(),
                "Doctor"
        );

        for (PatientRecord record : records) {
            System.out.println(record.getRecordType() + ": " + record.getMeasurementValue());
        }

        DeletionPolicy deletionPolicy = new DeletionPolicy(30);
        storage.applyDeletionPolicy(deletionPolicy);

        System.out.println(auditLog.getLogEntries());
    }
}