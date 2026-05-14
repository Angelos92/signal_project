package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.PatientDataParser;
import com.data_management.PatientRecord;

import org.junit.jupiter.api.Test;

class PatientDataParserTest {

    @Test
    void testParseWebSocketFormat() {
        PatientDataParser parser = new PatientDataParser();

        PatientRecord record = parser.parse("1,1000,Saturation,99.0%");

        assertEquals(1, record.getPatientId());
        assertEquals(1000L, record.getTimestamp());
        assertEquals("BloodSaturation", record.getRecordType());
        assertEquals(99.0, record.getMeasurementValue());
    }

    @Test
    void testParseFileFormat() {
        PatientDataParser parser = new PatientDataParser();

        PatientRecord record = parser.parse(
                "Patient ID: 2, Timestamp: 2000, Label: ECG, Data: 0.7"
        );

        assertEquals(2, record.getPatientId());
        assertEquals(2000L, record.getTimestamp());
        assertEquals("ECG", record.getRecordType());
        assertEquals(0.7, record.getMeasurementValue());
    }

    @Test
    void testInvalidMessageThrowsException() {
        PatientDataParser parser = new PatientDataParser();

        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse("This is not valid data");
        });
    }
}