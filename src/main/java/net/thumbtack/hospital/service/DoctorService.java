package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.MedicalCommission;
import net.thumbtack.hospital.util.TicketToCommissionBuilder;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.validator.annotation.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service("DoctorService")
public class DoctorService {
    private final DoctorDao doctorDao;

    @Autowired
    public DoctorService(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    public CreateMedicalCommissionDtoResponse createMedicalCommission(String sessionId,
                                                                      CreateMedicalCommissionDtoRequest request) throws PermissionDeniedException {
        doctorDao.hasPermissions(sessionId);

        List<Doctor> doctors = new ArrayList<>(request.getDoctorIds().size());

        for (int id : request.getDoctorIds()) {
            doctors.add(doctorDao.getDoctorById(id));
        }

        MedicalCommission commission =
                new MedicalCommission(LocalDate.parse(request.getDate()), LocalTime.parse(request.getTime()),
                request.getPatientId(), Integer.parseInt(request.getDuration()), doctors);

        doctorDao.createMedicalCommission(commission);

        return new CreateMedicalCommissionDtoResponse(
                TicketToCommissionBuilder.buildTicketNumber(request.getDoctorIds(), commission.getDate(), commission.getTime()),
                request.getPatientId(), request.getDoctorIds(), request.getRoom(), request.getDate(), request.getTime(),
                Integer.parseInt(request.getDuration()));
    }
}