package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import org.junit.jupiter.api.Test;

import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(
                1,
                1714376789050L,
                1714376789051L
        );

        assertEquals(2, records.size());
        assertEquals(100.0, records.get(0).getMeasurementValue());
        assertEquals("WhiteBloodCells", records.get(0).getRecordType());
        assertEquals(1, records.get(0).getPatientId());
    }

    @Test
    void testGetRecordsReturnsEmptyListForUnknownPatient() {
        DataStorage storage = new DataStorage();

        List<PatientRecord> records = storage.getRecords(
                999,
                1000L,
                2000L
        );

        assertTrue(records.isEmpty());
    }

    @Test
    void testGetPatientReturnsCorrectPatient() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(5, 98.0, "Saturation", 1000L);

        Patient patient = storage.getPatient(5);

        assertNotNull(patient);
        assertEquals(5, patient.getPatientId());
    }

    @Test
    void testGetAllPatientsReturnsAllPatients() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 98.0, "Saturation", 1000L);
        storage.addPatientData(2, 120.0, "SystolicPressure", 1001L);

        List<Patient> patients = storage.getAllPatients();

        assertEquals(2, patients.size());
    }
}