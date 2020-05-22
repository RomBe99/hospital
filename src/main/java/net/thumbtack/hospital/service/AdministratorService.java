package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.AdminDao;
import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtoresponse.admin.AdminRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditAdminProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditDoctorScheduleDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.model.Administrator;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.util.adapter.DtoAdapters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service("AdministratorService")
public class AdministratorService {
    private final AdminDao adminDao;
    private final DoctorDao doctorDao;

    @Autowired
    public AdministratorService(AdminDao adminDao, DoctorDao doctorDao) {
        this.adminDao = adminDao;
        this.doctorDao = doctorDao;
    }

    public AdminRegistrationDtoResponse administratorRegistration(String sessionId, AdminRegistrationDtoRequest request) {
        adminDao.hasPermissions(sessionId);

        Administrator a =
                new Administrator(request.getLogin(), request.getPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());

        adminDao.insertAdministrator(a);

        return new AdminRegistrationDtoResponse(a.getId(),
                a.getFirstName(), a.getLastName(), a.getPatronymic(), a.getPosition());
    }

    public DoctorRegistrationDtoResponse doctorRegistration(String sessionId, DoctorRegistrationDtoRequest request) {
        adminDao.hasPermissions(sessionId);

        Doctor d =
                new Doctor(request.getLogin(), request.getPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(),
                        "", request.getSpeciality());

        doctorDao.insertDoctor(d);

        return new DoctorRegistrationDtoResponse(d.getId(),
                d.getFirstName(), d.getLastName(), d.getPatronymic(), d.getSpecialty(), d.getCabinet(),
                d.getSchedule().stream()
                        .map(DtoAdapters::scheduleCellToScheduleCellResponse)
                        .collect(Collectors.toList()));
    }

    public EditAdminProfileDtoResponse editAdministratorProfile(String sessionId, EditAdminProfileDtoRequest request) {
        int adminId = adminDao.hasPermissions(sessionId);

        Administrator a =
                new Administrator(adminId, request.getNewPassword(),
                request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());

        adminDao.updateAdministrator(a);

        return new EditAdminProfileDtoResponse(adminId,
                request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());
    }

    // TODO
    public EditDoctorScheduleDtoResponse editDoctorSchedule(String sessionId, int doctorId, EditDoctorScheduleDtoRequest request) {
        adminDao.hasPermissions(sessionId);

        return null;
    }

    public EmptyDtoResponse removeDoctor(String sessionId, int doctorId, RemoveDoctorDtoRequest request) {
        adminDao.hasPermissions(sessionId);
        // TODO Что делать с датой увольнения из request?
        doctorDao.removeDoctor(doctorId);

        return new EmptyDtoResponse();
    }
}