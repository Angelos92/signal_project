package alerts;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import com.alerts.BasicAlert;
import com.alerts.decorator.PriorityAlertDecorator;
import com.alerts.decorator.RepeatedAlertDecorator;

import org.junit.jupiter.api.Test;

class AlertDecoratorTest {

    @Test
    void testPriorityAlertDecoratorAddsPriorityToCondition() {
        Alert alert = new BasicAlert("1", "Critical systolic blood pressure", 1000L);

        Alert priorityAlert = new PriorityAlertDecorator(alert, "HIGH");

        assertEquals("1", priorityAlert.getPatientId());
        assertEquals(1000L, priorityAlert.getTimestamp());
        assertTrue(priorityAlert.getCondition().contains("HIGH PRIORITY"));
        assertTrue(priorityAlert.getCondition().contains("Critical systolic blood pressure"));
    }

    @Test
    void testRepeatedAlertDecoratorAddsRepeatCountToCondition() {
        Alert alert = new BasicAlert("1", "Low oxygen saturation", 1000L);

        Alert repeatedAlert = new RepeatedAlertDecorator(alert, 2);

        assertEquals("1", repeatedAlert.getPatientId());
        assertEquals(1000L, repeatedAlert.getTimestamp());
        assertTrue(repeatedAlert.getCondition().contains("Low oxygen saturation"));
        assertTrue(repeatedAlert.getCondition().contains("repeated 2 times"));
    }

    @Test
    void testDecoratorsCanBeCombined() {
        Alert alert = new BasicAlert("1", "Manual alert triggered", 1000L);

        Alert priorityAlert = new PriorityAlertDecorator(alert, "HIGH");
        Alert repeatedPriorityAlert = new RepeatedAlertDecorator(priorityAlert, 3);

        assertTrue(repeatedPriorityAlert.getCondition().contains("HIGH PRIORITY"));
        assertTrue(repeatedPriorityAlert.getCondition().contains("Manual alert triggered"));
        assertTrue(repeatedPriorityAlert.getCondition().contains("repeated 3 times"));
    }
}