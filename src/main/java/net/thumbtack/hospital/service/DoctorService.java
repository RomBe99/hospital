package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.MedicalCommissionDao;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.security.manager.SecurityManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("DoctorService")
public class DoctorService {
    private final MedicalCommissionDao medicalCommissionDao;

    @Autowired
    public DoctorService(MedicalCommissionDao medicalCommissionDao) {
        this.medicalCommissionDao = medicalCommissionDao;
    }

    public CreateMedicalCommissionDtoResponse createMedicalCommission(String sessionId,
                                                                      CreateMedicalCommissionDtoRequest request) throws PermissionDeniedException {
        SecurityManagerImpl
                .getSecurityManager(UserType.DOCTOR)
                .hasPermission(sessionId);

        TicketToMedicalCommission commission = new TicketToMedicalCommission();

        medicalCommissionDao.createMedicalCommission(commission);

        return new CreateMedicalCommissionDtoResponse(
                commission.getTicket(), commission.getPatientId(), commission.getDoctorIds(),
                commission.getRoom(), commission.getDate().toString(), commission.getTime().toString(), request.getDuration());
    }
}