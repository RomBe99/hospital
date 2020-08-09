package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.user.Patient;

public interface PatientDao extends UserDao {
    void insertPatient(Patient patient);

    void updatePatient(Patient patient);

    Patient getPatientById(int id);
}