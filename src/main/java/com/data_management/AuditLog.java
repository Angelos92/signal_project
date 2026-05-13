package com.data_management;

import java.util.ArrayList;
import java.util.List;

/**
 * Records access attempts to patient data.
 * This supports traceability and privacy auditing.
 */
public class AuditLog {
    private List<String> logEntries;

    /**
     * Constructs an empty audit log.
     */
    public AuditLog() {
        this.logEntries = new ArrayList<>();
    }

    /**
     * Records an access attempt.
     *
     * @param requesterRole the role of the requester
     * @param patientId the patient ID being accessed
     * @param granted whether access was granted
     */
    public void recordAccess(String requesterRole, int patientId, boolean granted) {
        long timestamp = System.currentTimeMillis();

        String entry = "timestamp=" + timestamp
                + ", role=" + requesterRole
                + ", patientId=" + patientId
                + ", granted=" + granted;

        logEntries.add(entry);
    }

    /**
     * Returns all audit log entries.
     *
     * @return list of access log entries
     */
    public List<String> getLogEntries() {
        return new ArrayList<>(logEntries);
    }
}