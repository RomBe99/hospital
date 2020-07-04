package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PatientDao extends UserDao {
    Patient insertPatient(Patient patient);

    void updatePatient(Patient patient);

    Patient getPatientById(int id);

    void removePatient(int id);

    void appointmentToDoctor(int patientId, int doctorId, LocalDate date, LocalTime time);

    void denyMedicalCommission(String ticket);

    void denyTicket(String ticket);

    List<TicketToDoctor> getTicketsToDoctor(int patientId);

    List<TicketToMedicalCommission> getTicketsToMedicalCommission(int patientId);
}