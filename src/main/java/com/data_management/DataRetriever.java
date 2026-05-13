package com.data_management;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles secure retrieval of patient records.
 * Medical staff should retrieve patient data through this class instead of
 * directly accessing DataStorage.
 */
public class DataRetriever {
    private DataStorage dataStorage;
    private AccessController accessController;
    private AuditLog auditLog;

    /**
     * Constructs a DataRetriever.
     *
     * @param dataStorage the storage system to query
     * @param accessController the component that checks access permissions
     * @param auditLog the component that records access attempts
     */
    public DataRetriever(
            DataStorage dataStorage,
            AccessController accessController,
            AuditLog auditLog
    ) {
        this.dataStorage = dataStorage;
        this.accessController = accessController;
        this.auditLog = auditLog;
    }

    /**
     * Retrieves patient records if the requester is authorized.
     *
     * @param patientId the patient ID
     * @param startTime the start of the time range
     * @param endTime the end of the time range
     * @param requesterRole the role requesting access, e.g. "Doctor"
     * @return patient records if access is granted, otherwise an empty list
     */
    public List<PatientRecord> getPatientRecords(
            int patientId,
            long startTime,
            long endTime,
            String requesterRole
    ) {
        boolean granted = accessController.hasAccess(requesterRole);
        auditLog.recordAccess(requesterRole, patientId, granted);

        if (!granted) {
            System.err.println("Access denied for role: " + requesterRole);
            return new ArrayList<>();
        }

        return dataStorage.getRecords(patientId, startTime, endTime);
    }
}