package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.Patient;

public interface PatientDao extends UserDao {
    Patient insertPatient(Patient patient);

    void updatePatient(Patient patient);

    Patient getPatientById(int id);

    void removePatient(int id);
}