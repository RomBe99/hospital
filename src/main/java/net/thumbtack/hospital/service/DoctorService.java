package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        TicketToMedicalCommission commission = new TicketToMedicalCommission();

        doctorDao.createMedicalCommission(commission);

        return new CreateMedicalCommissionDtoResponse(
                commission.getTicket(), commission.getPatientId(), commission.getDoctorIds(),
                commission.getRoom(), commission.getDate().toString(), commission.getTime().toString(), request.getDuration());
    }
}