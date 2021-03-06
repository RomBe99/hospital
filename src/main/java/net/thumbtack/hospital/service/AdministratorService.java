package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.AdministratorDao;
import net.thumbtack.hospital.dao.CommonDao;
import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.dao.ScheduleDao;
import net.thumbtack.hospital.dtorequest.admin.AdminRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.admin.EditAdminProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.admin.RemoveDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.admin.AdminRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditAdminProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditDoctorScheduleDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.util.adapter.DtoAdapters;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.error.ScheduleErrorCode;
import net.thumbtack.hospital.util.error.ScheduleException;
import net.thumbtack.hospital.util.security.SecurityManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service("AdministratorService")
public class AdministratorService {
    private final AdministratorDao administratorDao;
    private final DoctorDao doctorDao;
    private final ScheduleDao scheduleDao;
    private final CommonDao commonDao;
    private final DtoAdapters dtoAdapters;

    @Autowired
    public AdministratorService(AdministratorDao administratorDao, DoctorDao doctorDao, ScheduleDao scheduleDao, CommonDao commonDao, DtoAdapters dtoAdapters) {
        this.administratorDao = administratorDao;
        this.doctorDao = doctorDao;
        this.scheduleDao = scheduleDao;
        this.commonDao = commonDao;
        this.dtoAdapters = dtoAdapters;
    }

    public AdminRegistrationDtoResponse administratorRegistration(String sessionId, AdminRegistrationDtoRequest request) throws PermissionDeniedException {
        SecurityManagerImpl
                .getSecurityManager(UserType.ADMINISTRATOR)
                .hasPermission(sessionId);

        final var admin = new Administrator(request.getLogin(), request.getPassword(),
                request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());

        administratorDao.insertAdministrator(admin);

        return new AdminRegistrationDtoResponse(admin.getId(),
                admin.getFirstName(), admin.getLastName(), admin.getPatronymic(), admin.getPosition());
    }

    public DoctorRegistrationDtoResponse doctorRegistration(String sessionId, DoctorRegistrationDtoRequest request) throws PermissionDeniedException {
        SecurityManagerImpl
                .getSecurityManager(UserType.ADMINISTRATOR)
                .hasPermission(sessionId);

        final var doctor = new Doctor(request.getLogin(), request.getPassword(),
                request.getFirstName(), request.getLastName(), request.getPatronymic(),
                request.getRoom(), request.getSpeciality(), new ArrayList<>());

        doctorDao.insertDoctor(doctor);

        final var doctorId = doctor.getId();
        final var schedule = dtoAdapters.transform(request, doctorId);

        if (!schedule.isEmpty()) {
            scheduleDao.insertSchedule(doctorId, schedule);
            doctor.setSchedule(schedule);
        }

        return new DoctorRegistrationDtoResponse(doctorId,
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                doctor.getSpecialty(), doctor.getCabinet(), dtoAdapters.transform(doctor.getSchedule()));
    }

    public EditAdminProfileDtoResponse editAdministratorProfile(String sessionId, EditAdminProfileDtoRequest request) throws PermissionDeniedException {
        final var adminId = SecurityManagerImpl
                .getSecurityManager(UserType.ADMINISTRATOR)
                .hasPermission(sessionId);

        final var admin = new Administrator(adminId, null, request.getOldPassword(),
                request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());

        administratorDao.updateAdministrator(admin, request.getNewPassword());

        return new EditAdminProfileDtoResponse(adminId,
                request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());
    }

    public EditDoctorScheduleDtoResponse editDoctorSchedule(String sessionId, int doctorId, DtoRequestWithSchedule request)
            throws PermissionDeniedException, ScheduleException {
        SecurityManagerImpl
                .getSecurityManager(UserType.ADMINISTRATOR)
                .hasPermission(sessionId);

        final var doctor = doctorDao.getDoctorById(doctorId);
        final var dateStart = LocalDate.parse(request.getDateStart());
        final var dateEnd = LocalDate.parse(request.getDateEnd());

        final var containsAppointments = commonDao.containsAppointments(doctorId, dateStart, dateEnd);

        if (containsAppointments) {
            throw new ScheduleException(ScheduleErrorCode.SCHEDULE_HAVE_APPOINTMENT);
        }

        final var schedule = dtoAdapters.transform(request, doctorId);

        if (!schedule.isEmpty()) {
            scheduleDao.editSchedule(doctorId, dateStart, dateEnd, schedule);
        }

        return new EditDoctorScheduleDtoResponse(doctor.getId(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                doctor.getSpecialty(), doctor.getCabinet(), dtoAdapters.transform(schedule));
    }

    public EmptyDtoResponse removeDoctor(String sessionId, int doctorId, RemoveDoctorDtoRequest request) throws PermissionDeniedException {
        SecurityManagerImpl
                .getSecurityManager(UserType.ADMINISTRATOR)
                .hasPermission(sessionId);

        doctorDao.removeDoctor(doctorId);

        return new EmptyDtoResponse();
    }
}