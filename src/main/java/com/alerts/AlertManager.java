package com.alerts;

import com.alerts.decorator.PriorityAlertDecorator;
import com.alerts.decorator.RepeatedAlertDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * The AlertManager class is responsible for dispatching alerts
 * to medical staff or other alert receivers.
 */
public class AlertManager {
    private List<Alert> dispatchedAlerts;

    /**
     * Constructs an AlertManager with an empty alert history.
     */
    public AlertManager() {
        this.dispatchedAlerts = new ArrayList<>();
    }

    /**
     * Dispatches an alert to the monitoring system.
     * Decorators can add extra behavior such as priority or repeated-alert tags.
     *
     * @param alert the alert to dispatch
     */
    public void dispatchAlert(Alert alert) {
        Alert decoratedAlert = decorateAlert(alert);

        dispatchedAlerts.add(decoratedAlert);

        System.out.println(
                "ALERT DISPATCHED | Patient: " + decoratedAlert.getPatientId()
                        + " | Condition: " + decoratedAlert.getCondition()
                        + " | Timestamp: " + decoratedAlert.getTimestamp()
        );
    }

    /**
     * Adds decorators to alerts when extra behavior is needed.
     *
     * @param alert the original alert
     * @return the decorated alert
     */
    private Alert decorateAlert(Alert alert) {
        Alert decoratedAlert = alert;

        if (isHighPriorityAlert(alert)) {
            decoratedAlert = new PriorityAlertDecorator(decoratedAlert, "HIGH");
        }

        int repeatCount = countPreviousSimilarAlerts(alert) + 1;

        if (repeatCount > 1) {
            decoratedAlert = new RepeatedAlertDecorator(decoratedAlert, repeatCount);
        }

        return decoratedAlert;
    }

    /**
     * Determines whether an alert should be marked as high priority.
     *
     * @param alert the alert to check
     * @return true if the alert is urgent
     */
    private boolean isHighPriorityAlert(Alert alert) {
        String condition = alert.getCondition().toLowerCase();

        return condition.contains("critical")
                || condition.contains("hypotensive")
                || condition.contains("manual")
                || condition.contains("rapid");
    }

    /**
     * Counts previous alerts for the same patient and same condition.
     *
     * @param alert the new alert
     * @return number of previous similar alerts
     */
    private int countPreviousSimilarAlerts(Alert alert) {
        int count = 0;

        for (Alert existingAlert : dispatchedAlerts) {
            boolean samePatient = existingAlert.getPatientId().equals(alert.getPatientId());
            boolean sameCondition = existingAlert.getCondition().contains(alert.getCondition());

            if (samePatient && sameCondition) {
                count++;
            }
        }

        return count;
    }

    /**
     * Returns all alerts that have been dispatched.
     *
     * @return a list of dispatched alerts
     */
    public List<Alert> getDispatchedAlerts() {
        return dispatchedAlerts;
    }
}