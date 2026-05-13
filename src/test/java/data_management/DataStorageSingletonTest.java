package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataStorage;

import org.junit.jupiter.api.Test;

class DataStorageSingletonTest {

    @Test
    void testDataStorageReturnsSameInstance() {
        DataStorage firstInstance = DataStorage.getInstance();
        DataStorage secondInstance = DataStorage.getInstance();

        assertSame(firstInstance, secondInstance);
    }

    @Test
    void testDataIsSharedAcrossSingletonReferences() {
        DataStorage firstInstance = DataStorage.getInstance();
        firstInstance.clear();

        DataStorage secondInstance = DataStorage.getInstance();

        firstInstance.addPatientData(1, 98.0, "BloodSaturation", 1000L);

        assertEquals(1, secondInstance.getRecords(1, 1000L, 1000L).size());
    }
}