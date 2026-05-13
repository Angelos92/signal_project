package com.patient_identification;

/**
 * Links a simulator patient ID to a real hospital patient ID.
 */
public class PatientIDMapping {
    private int simulatorPatientId;
    private String hospitalId;

    public PatientIDMapping(int simulatorPatientId, String hospitalId) {
        this.simulatorPatientId = simulatorPatientId;
        this.hospitalId = hospitalId;
    }

    public int getSimulatorPatientId() {
        return simulatorPatientId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public boolean matchesSimulatorId(int simulatorPatientId) {
        return this.simulatorPatientId == simulatorPatientId;
    }
}