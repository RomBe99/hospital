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
        final int commissionCreatorId = SecurityManagerImpl
                .getSecurityManager(UserType.DOCTOR)
                .hasPermission(sessionId);

        final LocalDate date = LocalDate.parse(request.getDate());
        final LocalTime time = LocalTime.parse(request.getTime());
        final List<Integer> doctorIds = request.getDoctorIds();

        if (!doctorIds.contains(commissionCreatorId)) {
            doctorIds.add(commissionCreatorId);
        }

        final String title = TicketFactory.buildTicketToCommission(date, time, doctorIds);
        final int patientId = request.getPatientId();
        final int duration = request.getDuration();
        final String room = request.getRoom();

        final TicketToMedicalCommission commission =
                new TicketToMedicalCommission(title, room, date, time, patientId, doctorIds, duration);

        medicalCommissionDao.createMedicalCommission(commission);

        return new CreateMedicalCommissionDtoResponse(
                commission.getTitle(), commission.getPatientId(), commission.getDoctorIds(), commission.getRoom(),
                commission.getDate().toString(), commission.getTime().toString(), request.getDuration());
    }
}