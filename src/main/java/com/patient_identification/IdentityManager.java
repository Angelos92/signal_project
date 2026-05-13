package com.patient_identification;

/**
 * Coordinates patient identification and handles mismatches.
 */
public class IdentityManager {
    private PatientIdentifier patientIdentifier;
    private IdentityMismatchHandler mismatchHandler;

    public IdentityManager(PatientIdentifier patientIdentifier, IdentityMismatchHandler mismatchHandler) {
        this.patientIdentifier = patientIdentifier;
        this.mismatchHandler = mismatchHandler;
    }

    public HospitalPatient resolvePatient(int simulatorPatientId) {
        HospitalPatient hospitalPatient = patientIdentifier.identifyPatient(simulatorPatientId);

        if (hospitalPatient == null) {
            mismatchHandler.handleUnknownPatient(simulatorPatientId);
        }

        return hospitalPatient;
    }
}