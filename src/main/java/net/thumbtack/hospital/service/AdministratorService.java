package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.AdminDao;
import net.thumbtack.hospital.dao.DoctorDao;
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
import net.thumbtack.hospital.dtoresponse.schedule.DtoResponseWithSchedule;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.util.DtoAdapters;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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

    private DtoResponseWithSchedule insertSchedule(DtoRequestWithSchedule request, int doctorId) {
        // TODO Что делать, если в запросе присутсвуют несколько видов расписаний?
        List<ScheduleCell> schedule = DtoAdapters.transform(request, doctorId);

        if (doctorId == 0 || schedule.isEmpty()) {
            return new DtoResponseWithSchedule() {
                @Override
                public void setSchedule(List<ScheduleCellDtoResponse> schedule) {
                    super.setSchedule(schedule);
                }

                @Override
                public List<ScheduleCellDtoResponse> getSchedule() {
                    return super.getSchedule();
                }

                @Override
                public boolean equals(Object o) {
                    return super.equals(o);
                }

                @Override
                public int hashCode() {
                    return super.hashCode();
                }

                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }

        schedule.sort(Comparator.comparing(ScheduleCell::getDate));
        schedule.forEach(sc -> sc.getCells().sort(Comparator.comparing(TimeCell::getTime)));

        adminDao.editDoctorSchedule(LocalDate.parse(request.getDateStart()), LocalDate.parse(request.getDateEnd()), doctorId, schedule);

        return new DtoResponseWithSchedule(schedule.stream()
                .map(DtoAdapters::transform)
                .collect(Collectors.toList())) {
            @Override
            public void setSchedule(List<ScheduleCellDtoResponse> schedule) {
                super.setSchedule(schedule);
            }

            @Override
            public List<ScheduleCellDtoResponse> getSchedule() {
                return super.getSchedule();
            }

            @Override
            public boolean equals(Object o) {
                return super.equals(o);
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public String toString() {
                return super.toString();
            }
        };
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
                        request.getRoom(), request.getSpeciality(), new ArrayList<>());

        doctorDao.insertDoctor(doctor);

        DtoResponseWithSchedule responseWithSchedule = insertSchedule(request, doctor.getId());

        return new DoctorRegistrationDtoResponse(doctor.getId(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                doctor.getSpecialty(), doctor.getCabinet(), responseWithSchedule.getSchedule());
    }

    public EditAdminProfileDtoResponse editAdministratorProfile(String sessionId, EditAdminProfileDtoRequest request) throws PermissionDeniedException {
        int adminId = adminDao.hasPermissions(sessionId);

        Administrator admin =
                new Administrator(adminId, null, request.getNewPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());

        adminDao.updateAdministrator(admin);

        return new EditAdminProfileDtoResponse(adminId,
                request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition());
    }

    public EditDoctorScheduleDtoResponse editDoctorSchedule(String sessionId, int doctorId, DtoRequestWithSchedule request) throws PermissionDeniedException {
        adminDao.hasPermissions(sessionId);

        Doctor doctor = doctorDao.getDoctorById(doctorId);

        DtoResponseWithSchedule responseWithSchedule = insertSchedule(request, doctor.getId());

        return new EditDoctorScheduleDtoResponse(doctor.getId(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                doctor.getSpecialty(), doctor.getCabinet(), responseWithSchedule.getSchedule());
    }

    public EmptyDtoResponse removeDoctor(String sessionId, int doctorId, RemoveDoctorDtoRequest request) throws PermissionDeniedException {
        adminDao.hasPermissions(sessionId);
        doctorDao.removeDoctor(doctorId);

        return new EmptyDtoResponse();
    }
}