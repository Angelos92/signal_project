package com.alerts.decorator;

import com.alerts.Alert;

/**
 * Adds repeated-alert information to an alert.
 */
public class RepeatedAlertDecorator extends AlertDecorator {
    private int repeatCount;

    public RepeatedAlertDecorator(Alert wrappedAlert, int repeatCount) {
        super(wrappedAlert);
        this.repeatCount = repeatCount;
    }

    @Override
    public String getCondition() {
        return wrappedAlert.getCondition() + " | repeated " + repeatCount + " times";
    }

    public int getRepeatCount() {
        return repeatCount;
    }
}