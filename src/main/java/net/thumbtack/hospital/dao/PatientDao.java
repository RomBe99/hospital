package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.Patient;

import java.time.LocalDate;
import java.time.LocalTime;

public interface PatientDao extends UserDao {
    Patient insertPatient(Patient patient);

    void updatePatient(Patient patient);

    Patient getPatientById(int id);

    void removePatient(int id);

    void appointmentToDoctor(int patientId, int doctorId, LocalDate date, LocalTime time);

    void denyMedicalCommission(int patientId, int commissionTicketId);

    void denyTicket(int patientId, int scheduleCellId);
}