package com.alerts;

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
     * For now, this method stores the alert and prints it to the console.
     * Later, it could notify medical staff, send an email, or write to a log.
     *
     * @param alert the alert to dispatch
     */
    public void dispatchAlert(Alert alert) {
        dispatchedAlerts.add(alert);

        System.out.println(
                "ALERT DISPATCHED | Patient: " + alert.getPatientId()
                        + " | Condition: " + alert.getCondition()
                        + " | Timestamp: " + alert.getTimestamp()
        );
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
