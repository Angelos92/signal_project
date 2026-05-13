package com.patient_identification;

import java.util.List;

/**
 * Matches simulator patient IDs to hospital patient records.
 */
public class PatientIdentifier {
    private List<PatientIDMapping> mappings;
    private List<HospitalPatient> hospitalPatients;

    public PatientIdentifier(List<PatientIDMapping> mappings, List<HospitalPatient> hospitalPatients) {
        this.mappings = mappings;
        this.hospitalPatients = hospitalPatients;
    }

    public HospitalPatient identifyPatient(int simulatorPatientId) {
        for (PatientIDMapping mapping : mappings) {
            if (mapping.matchesSimulatorId(simulatorPatientId)) {
                return findHospitalPatient(mapping.getHospitalId());
            }
        }

        return null;
    }

    private HospitalPatient findHospitalPatient(String hospitalId) {
        for (HospitalPatient patient : hospitalPatients) {
            if (patient.getHospitalId().equals(hospitalId)) {
                return patient;
            }
        }

        return null;
    }
}