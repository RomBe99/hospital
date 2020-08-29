package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.MedicalCommissionDao;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.security.SecurityManagerImpl;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service("DoctorService")
public class DoctorService {
    private final MedicalCommissionDao medicalCommissionDao;

    @Autowired
    public DoctorService(MedicalCommissionDao medicalCommissionDao) {
        this.medicalCommissionDao = medicalCommissionDao;
    }

    public CreateMedicalCommissionDtoResponse createMedicalCommission(String sessionId,
                                                                      CreateMedicalCommissionDtoRequest request) throws PermissionDeniedException {
        int commissionCreatorId = SecurityManagerImpl
                .getSecurityManager(UserType.DOCTOR)
                .hasPermission(sessionId);

        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime time = LocalTime.parse(request.getTime());
        List<Integer> doctorIds = request.getDoctorIds();

        if (!doctorIds.contains(commissionCreatorId)) {
            doctorIds.add(commissionCreatorId);
        }

        String title = TicketFactory.buildTicketToCommission(date, time, doctorIds);
        int patientId = request.getPatientId();
        int duration = request.getDuration();
        String room = request.getRoom();

        TicketToMedicalCommission commission =
                new TicketToMedicalCommission(title, room, date, time, patientId, doctorIds, duration);

        medicalCommissionDao.createMedicalCommission(commission);

        return new CreateMedicalCommissionDtoResponse(
                commission.getTitle(), commission.getPatientId(), commission.getDoctorIds(), commission.getRoom(),
                commission.getDate().toString(), commission.getTime().toString(), request.getDuration());
    }
}