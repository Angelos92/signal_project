package alerts;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import com.alerts.AlertManager;
import com.alerts.BasicAlert;

import org.junit.jupiter.api.Test;

import java.util.List;

class AlertManagerTest {

    @Test
    void testCriticalAlertIsMarkedHighPriority() {
        AlertManager alertManager = new AlertManager();

        Alert alert = new BasicAlert("1", "Critical systolic blood pressure", 1000L);

        alertManager.dispatchAlert(alert);

        List<Alert> alerts = alertManager.getDispatchedAlerts();

        assertEquals(1, alerts.size());
        assertTrue(alerts.get(0).getCondition().contains("HIGH PRIORITY"));
    }

    @Test
    void testRepeatedAlertIsMarkedRepeated() {
        AlertManager alertManager = new AlertManager();

        Alert firstAlert = new BasicAlert("1", "Critical systolic blood pressure", 1000L);
        Alert secondAlert = new BasicAlert("1", "Critical systolic blood pressure", 2000L);

        alertManager.dispatchAlert(firstAlert);
        alertManager.dispatchAlert(secondAlert);

        List<Alert> alerts = alertManager.getDispatchedAlerts();

        assertEquals(2, alerts.size());
        assertTrue(alerts.get(1).getCondition().contains("repeated 2 times"));
    }
}