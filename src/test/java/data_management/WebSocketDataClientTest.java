package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.websocket.WebSocketDataClient;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

class WebSocketDataClientTest {

    @Test
    void testProcessValidWebSocketMessageStoresData() throws Exception {
        DataStorage storage = DataStorage.getInstance();
        storage.clear();

        WebSocketDataClient client = new WebSocketDataClient(
                new URI("ws://localhost:8080"),
                storage
        );

        client.processMessage("1,1000,Saturation,97.0%");

        List<PatientRecord> records = storage.getRecords(1, 1000L, 1000L);

        assertEquals(1, records.size());
        assertEquals("BloodSaturation", records.get(0).getRecordType());
        assertEquals(97.0, records.get(0).getMeasurementValue());
    }

    @Test
    void testProcessInvalidWebSocketMessageDoesNotCrash() throws Exception {
        DataStorage storage = DataStorage.getInstance();
        storage.clear();

        WebSocketDataClient client = new WebSocketDataClient(
                new URI("ws://localhost:8080"),
                storage
        );

        client.processMessage("corrupted message");

        assertTrue(storage.getAllPatients().isEmpty());
        assertNotNull(client.getLastErrorMessage());
        assertTrue(client.getLastErrorMessage().contains("Invalid message skipped"));
    }

    @Test
    void testProcessBlankMessageDoesNotCrash() throws Exception {
        DataStorage storage = DataStorage.getInstance();
        storage.clear();

        WebSocketDataClient client = new WebSocketDataClient(
                new URI("ws://localhost:8080"),
                storage
        );

        client.processMessage("");

        assertTrue(storage.getAllPatients().isEmpty());
        assertNotNull(client.getLastErrorMessage());
    }
}