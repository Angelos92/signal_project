package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import org.junit.jupiter.api.Test;

import java.util.List;

class PatientTest {

    @Test
    void testGetRecordsFiltersByTimeRange() {
        Patient patient = new Patient(1);

        patient.addRecord(100.0, "HeartRate", 1000L);
        patient.addRecord(110.0, "HeartRate", 2000L);
        patient.addRecord(120.0, "HeartRate", 3000L);

        List<PatientRecord> records = patient.getRecords(1500L, 2500L);

        assertEquals(1, records.size());
        assertEquals(110.0, records.get(0).getMeasurementValue());
    }

    @Test
    void testGetRecordsIncludesBoundaryTimes() {
        Patient patient = new Patient(1);

        patient.addRecord(100.0, "HeartRate", 1000L);
        patient.addRecord(120.0, "HeartRate", 3000L);

        List<PatientRecord> records = patient.getRecords(1000L, 3000L);

        assertEquals(2, records.size());
    }

    @Test
    void testGetRecordsReturnsEmptyWhenNoRecordsInRange() {
        Patient patient = new Patient(1);

        patient.addRecord(100.0, "HeartRate", 1000L);

        List<PatientRecord> records = patient.getRecords(2000L, 3000L);

        assertTrue(records.isEmpty());
    }
}