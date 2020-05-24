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
import net.thumbtack.hospital.model.ScheduleCell;
import net.thumbtack.hospital.util.DtoAdapters;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

    public AdminRegistrationDtoResponse administratorRegistration(String sessionId, AdminRegistrationDtoRequest request) throws PermissionDeniedException {
        adminDao.hasPermissions(sessionId);

        Administrator admin =
                new Administrator(request.getLogin(), request.getPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());

        adminDao.insertAdministrator(admin);

        return new AdminRegistrationDtoResponse(admin.getId(),
                admin.getFirstName(), admin.getLastName(), admin.getPatronymic(), admin.getPosition());
    }

    public DoctorRegistrationDtoResponse doctorRegistration(String sessionId, DoctorRegistrationDtoRequest request) throws PermissionDeniedException {
        adminDao.hasPermissions(sessionId);

        Doctor doctor =
                new Doctor(request.getLogin(), request.getPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(),
                        "", request.getSpeciality());

        doctorDao.insertDoctor(doctor);

        return new DoctorRegistrationDtoResponse(doctor.getId(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpecialty(), doctor.getCabinet(),
                doctor.getSchedule().stream()
                        .map(DtoAdapters::scheduleCellToScheduleCellResponse)
                        .collect(Collectors.toList()));
    }

    public EditAdminProfileDtoResponse editAdministratorProfile(String sessionId, EditAdminProfileDtoRequest request) throws PermissionDeniedException {
        int adminId = adminDao.hasPermissions(sessionId);

        Administrator admin =
                new Administrator(adminId, request.getNewPassword(),
                request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());

        adminDao.updateAdministrator(admin);

        return new EditAdminProfileDtoResponse(adminId,
                request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());
    }

    public EditDoctorScheduleDtoResponse editDoctorSchedule(String sessionId, int doctorId, EditDoctorScheduleDtoRequest request) throws PermissionDeniedException {
        adminDao.hasPermissions(sessionId);

        List<ScheduleCell> scheduleCells;

        if (request.getWeekSchedule() != null && !request.getWeekSchedule().isEmpty()) {
            scheduleCells = DtoAdapters.weekScheduleCellsToScheduleCells(request.getWeekSchedule());
        } else if (request.getWeekDaysSchedule() != null && !request.getWeekDaysSchedule().isEmpty()) {
            scheduleCells = DtoAdapters.weekDayScheduleCellsToScheduleCells(request.getWeekDaysSchedule());
        } else {
            throw new RuntimeException("Request contains empty schedules");
        }

        adminDao.editDoctorSchedule(LocalDate.parse(request.getDateStart()),
                LocalDate.parse(request.getDateEnd()), doctorId, scheduleCells);

        Doctor doctor = doctorDao.getDoctorById(doctorId);

        return new EditDoctorScheduleDtoResponse(doctor.getId(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                doctor.getSpecialty(), doctor.getCabinet(),
                doctor.getSchedule().stream()
                        .map(DtoAdapters::scheduleCellToScheduleCellResponse)
                        .collect(Collectors.toList()));
    }

    public EmptyDtoResponse removeDoctor(String sessionId, int doctorId, RemoveDoctorDtoRequest request) throws PermissionDeniedException {
        adminDao.hasPermissions(sessionId);
        doctorDao.removeDoctor(doctorId);

        return new EmptyDtoResponse();
    }
}