package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class FileDataReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void testReadDataFromGeneratedFile() throws IOException {
        Path file = tempDir.resolve("patient_data.txt");

        Files.writeString(
                file,
                "Patient ID: 1, Timestamp: 1000, Label: Saturation, Data: 99.0%\n" +
                "Patient ID: 1, Timestamp: 1001, Label: ECG, Data: 0.5\n"
        );

        DataStorage storage = new DataStorage();
        FileDataReader reader = new FileDataReader(tempDir.toString());

        reader.readData(storage);

        List<PatientRecord> records = storage.getRecords(1, 1000L, 1001L);

        assertEquals(2, records.size());
        assertEquals(99.0, records.get(0).getMeasurementValue());
        assertEquals("BloodSaturation", records.get(0).getRecordType());
        assertEquals("ECG", records.get(1).getRecordType());
    }

    @Test
    void testInvalidLinesAreSkipped() throws IOException {
        Path file = tempDir.resolve("patient_data.txt");

        Files.writeString(
                file,
                "This is not valid data\n" +
                "Patient ID: 2, Timestamp: 2000, Label: ECG, Data: 0.7\n"
        );

        DataStorage storage = new DataStorage();
        FileDataReader reader = new FileDataReader(tempDir.toString());

        reader.readData(storage);

        List<PatientRecord> records = storage.getRecords(2, 2000L, 2000L);

        assertEquals(1, records.size());
        assertEquals(0.7, records.get(0).getMeasurementValue());
    }
}