package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;

import java.util.List;

public interface DoctorDao extends UserDao {
    Doctor insertDoctor(Doctor doctor);

    void removeDoctor(int id);

    List<Doctor> getAllDoctors();

    Doctor getDoctorById(int id);

    void createMedicalCommission(TicketToMedicalCommission ticketToMedicalCommission);
}