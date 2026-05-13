package com.alerts.decorator;

import com.alerts.Alert;

/**
 * Adds priority information to an alert without changing the original alert class.
 */
public class PriorityAlertDecorator extends AlertDecorator {
    private String priorityLevel;

    public PriorityAlertDecorator(Alert wrappedAlert, String priorityLevel) {
        super(wrappedAlert);
        this.priorityLevel = priorityLevel;
    }

    @Override
    public String getCondition() {
        return "[" + priorityLevel + " PRIORITY] " + wrappedAlert.getCondition();
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }
}