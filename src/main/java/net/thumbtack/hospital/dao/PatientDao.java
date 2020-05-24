package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.MedicalCommission;
import net.thumbtack.hospital.model.Patient;
import net.thumbtack.hospital.model.ScheduleCell;
import net.thumbtack.hospital.model.TicketToDoctor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PatientDao extends UserDao {
    Patient insertPatient(Patient patient);

    void updatePatient(Patient patient);

    Patient getPatientById(int id);

    void removePatient(int id);

    void appointmentToDoctor(int patientId, int doctorId, LocalDate date, LocalTime time);

    void denyMedicalCommission(int patientId, int commissionTicketId);

    void denyTicket(int patientId, int doctorId, LocalDate date, LocalTime time);

    List<TicketToDoctor> getTicketsToDoctor(int patientId);

    List<MedicalCommission> getTicketsToMedicalCommission(int patientId);
}