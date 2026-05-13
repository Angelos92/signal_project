package alerts;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.alerts.AlertManager;
import com.data_management.DataStorage;
import com.data_management.Patient;

import org.junit.jupiter.api.Test;

import java.util.List;

class AlertGeneratorTest {

    @Test
    void testLowOxygenSaturationAlert() {
        DataStorage storage = DataStorage.getInstance();
        storage.clear();
        AlertManager alertManager = new AlertManager();
        AlertGenerator alertGenerator = new AlertGenerator(storage, alertManager);

        long now = System.currentTimeMillis();
        storage.addPatientData(1, 91.0, "BloodSaturation", now);

        Patient patient = storage.getPatient(1);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertManager.getDispatchedAlerts();

        assertFalse(alerts.isEmpty());
        assertTrue(alerts.get(0).getCondition().contains("Low oxygen saturation"));
    }

    @Test
    void testCriticalSystolicPressureAlert() {
        DataStorage storage = DataStorage.getInstance();
        storage.clear();
        AlertManager alertManager = new AlertManager();
        AlertGenerator alertGenerator = new AlertGenerator(storage, alertManager);

        long now = System.currentTimeMillis();
        storage.addPatientData(1, 181.0, "SystolicPressure", now);

        Patient patient = storage.getPatient(1);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertManager.getDispatchedAlerts();

        assertFalse(alerts.isEmpty());
        assertTrue(alerts.get(0).getCondition().contains("Critical systolic"));
    }

    @Test
    void testCriticalDiastolicPressureAlert() {
        DataStorage storage = DataStorage.getInstance();
        storage.clear();
        AlertManager alertManager = new AlertManager();
        AlertGenerator alertGenerator = new AlertGenerator(storage, alertManager);

        long now = System.currentTimeMillis();
        storage.addPatientData(1, 121.0, "DiastolicPressure", now);

        Patient patient = storage.getPatient(1);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertManager.getDispatchedAlerts();

        assertFalse(alerts.isEmpty());
        assertTrue(alerts.get(0).getCondition().contains("Critical diastolic"));
    }

    @Test
    void testBloodPressureIncreasingTrendAlert() {
        DataStorage storage = DataStorage.getInstance();
        storage.clear();        
        AlertManager alertManager = new AlertManager();
        AlertGenerator alertGenerator = new AlertGenerator(storage, alertManager);

        long now = System.currentTimeMillis();

        storage.addPatientData(1, 100.0, "SystolicPressure", now - 3000);
        storage.addPatientData(1, 112.0, "SystolicPressure", now - 2000);
        storage.addPatientData(1, 125.0, "SystolicPressure", now - 1000);

        Patient patient = storage.getPatient(1);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertManager.getDispatchedAlerts();

        assertTrue(
                alerts.stream().anyMatch(alert ->
                        alert.getCondition().contains("increasing trend"))
        );
    }

    @Test
    void testRapidOxygenDropAlert() {
        DataStorage storage = DataStorage.getInstance();
        storage.clear();
        AlertManager alertManager = new AlertManager();
        AlertGenerator alertGenerator = new AlertGenerator(storage, alertManager);

        long now = System.currentTimeMillis();

        storage.addPatientData(1, 98.0, "BloodSaturation", now - 5000);
        storage.addPatientData(1, 92.0, "BloodSaturation", now);

        Patient patient = storage.getPatient(1);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertManager.getDispatchedAlerts();

        assertTrue(
                alerts.stream().anyMatch(alert ->
                        alert.getCondition().contains("Rapid oxygen saturation drop"))
        );
    }

    @Test
    void testHypotensiveHypoxemiaAlert() {
        DataStorage storage = DataStorage.getInstance();
        storage.clear();
        AlertManager alertManager = new AlertManager();
        AlertGenerator alertGenerator = new AlertGenerator(storage, alertManager);

        long now = System.currentTimeMillis();

        storage.addPatientData(1, 85.0, "SystolicPressure", now - 1000);
        storage.addPatientData(1, 91.0, "BloodSaturation", now);

        Patient patient = storage.getPatient(1);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertManager.getDispatchedAlerts();

        assertTrue(
                alerts.stream().anyMatch(alert ->
                        alert.getCondition().contains("Hypotensive hypoxemia"))
        );
    }

    @Test
    void testManualTriggeredAlert() {
        DataStorage storage = DataStorage.getInstance();
        storage.clear();
        AlertManager alertManager = new AlertManager();
        AlertGenerator alertGenerator = new AlertGenerator(storage, alertManager);

        long now = System.currentTimeMillis();

        storage.addPatientData(1, 1.0, "Alert", now);

        Patient patient = storage.getPatient(1);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertManager.getDispatchedAlerts();

        assertTrue(
                alerts.stream().anyMatch(alert ->
                        alert.getCondition().contains("Manual alert triggered"))
        );
    }
}