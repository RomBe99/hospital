package net.thumbtack.hospital.mappers;

import net.thumbtack.hospital.model.Patient;

public interface PatientMapper {
    int insertPatient(Patient patient);

    void updatePatient(Patient patient);

    void deleteAll();
}