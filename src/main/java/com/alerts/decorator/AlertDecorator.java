package com.alerts.decorator;

import com.alerts.Alert;

/**
 * Base decorator for Alert objects.
 * It wraps another Alert and delegates default behavior to it.
 */
public abstract class AlertDecorator implements Alert {
    protected Alert wrappedAlert;

    public AlertDecorator(Alert wrappedAlert) {
        this.wrappedAlert = wrappedAlert;
    }

    @Override
    public String getPatientId() {
        return wrappedAlert.getPatientId();
    }

    @Override
    public String getCondition() {
        return wrappedAlert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return wrappedAlert.getTimestamp();
    }
}