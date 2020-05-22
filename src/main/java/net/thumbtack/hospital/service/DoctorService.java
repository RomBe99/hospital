package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.model.MedicalCommission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service("DoctorService")
public class DoctorService {
    private final DoctorDao doctorDao;

    @Autowired
    public DoctorService(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    public CreateMedicalCommissionDtoResponse createMedicalCommission(String sessionId,
                                                                      CreateMedicalCommissionDtoRequest request) {
        doctorDao.hasPermissions(sessionId);

        MedicalCommission c = new MedicalCommission(LocalDate.parse(request.getDate()), LocalTime.parse(request.getTime()),
                request.getPatientId(), Integer.parseInt(request.getDuration()), request.getDoctorIds());

        int ticketId = doctorDao.createMedicalCommission(c);

        return new CreateMedicalCommissionDtoResponse(String.valueOf(ticketId), request.getPatientId(),
                request.getDoctorIds(), request.getRoom(), request.getDate(), request.getTime(),
                Integer.parseInt(request.getDuration()));
    }
}