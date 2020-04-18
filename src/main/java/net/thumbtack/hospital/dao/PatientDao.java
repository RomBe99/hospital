package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.Patient;

public interface PatientDao {
    Patient insertPatient(Patient patient);

    void updatePatient(Patient patient);
}