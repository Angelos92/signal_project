package com;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.AccessController;
import com.data_management.AuditLog;
import com.data_management.DataRetriever;
import com.data_management.DataStorage;
import com.data_management.DeletionPolicy;
import com.data_management.PatientRecord;

import java.util.List;

public class Main {


    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equalsIgnoreCase("DataStorage")) {
            runDataStorageDemo();
        } else {
            HealthDataSimulator.main(args);
        }
    }

    private static void runDataStorageDemo() {
        DataStorage storage = new DataStorage();

        long now = System.currentTimeMillis();
        storage.addPatientData(1, 145.0, "HeartRate", now);

        List<PatientRecord> records = storage.getRecords(1, now - 1000, now + 1000);

        for (PatientRecord record : records) {
            System.out.println(
                    "Patient ID: " + record.getPatientId()
                            + ", Type: " + record.getRecordType()
                            + ", Value: " + record.getMeasurementValue()
                            + ", Timestamp: " + record.getTimestamp()
            );
        }
    }
}