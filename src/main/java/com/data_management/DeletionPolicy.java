package com.data_management;

/**
 * Defines the rule for deleting old patient records.
 * Records older than the retention period should be removed.
 */
public class DeletionPolicy {
    private int retentionDays;

    /**
     * Constructs a deletion policy.
     *
     * @param retentionDays the number of days records should be kept
     */
    public DeletionPolicy(int retentionDays) {
        this.retentionDays = retentionDays;
    }

    /**
     * Checks whether a patient record should be deleted.
     *
     * @param record the patient record to check
     * @param currentTime the current time in milliseconds since epoch
     * @return true if the record is older than the retention period
     */
    public boolean shouldDelete(PatientRecord record, long currentTime) {
        long retentionMillis = retentionDays * 24L * 60L * 60L * 1000L;
        return currentTime - record.getTimestamp() > retentionMillis;
    }

    /**
     * Returns the retention period.
     *
     * @return retention period in days
     */
    public int getRetentionDays() {
        return retentionDays;
    }
}