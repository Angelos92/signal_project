package com.patient_identification;

import java.util.List;

/**
 * Represents a real patient record from the hospital database.
 */
public class HospitalPatient {
    private String hospitalId;
    private String fullName;
    private String dateOfBirth;
    private List<String> medicalHistory;

    public HospitalPatient(String hospitalId, String fullName, String dateOfBirth, List<String> medicalHistory) {
        this.hospitalId = hospitalId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.medicalHistory = medicalHistory;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public List<String> getMedicalHistory() {
        return medicalHistory;
    }
}