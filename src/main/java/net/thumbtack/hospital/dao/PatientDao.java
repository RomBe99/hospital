package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.user.Patient;

public interface PatientDao extends PermissionsDao {
    void insertPatient(Patient patient);

    void updatePatient(Patient patient, String newPassword);

    Patient getPatientById(int id);
}