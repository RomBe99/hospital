package net.thumbtack.hospital.debug;

import net.thumbtack.hospital.debug.dtoresponse.schedule.GetScheduleByDoctorIdDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;
import net.thumbtack.hospital.util.adapter.DtoAdapters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("DebugService")
public class DebugService {
    private final DebugDao debugDao;
    private final DtoAdapters dtoAdapters;

    @Autowired
    public DebugService(DebugDao debugDao, DtoAdapters dtoAdapters) {
        this.debugDao = debugDao;
        this.dtoAdapters = dtoAdapters;
    }

    public void clear() {
        debugDao.clearUsers();
        debugDao.clearLoggedInUsers();
        debugDao.clearAdministrators();
        debugDao.clearDoctors();
        debugDao.clearPatients();
        debugDao.clearMedicalCommissions();
        debugDao.clearCommissionDoctors();
        debugDao.clearScheduleCells();
        debugDao.clearTimeCells();
    }

    public GetScheduleByDoctorIdDtoResponse getScheduleByDoctorId(int doctorId) {
        List<ScheduleCellDtoResponse> responseSchedule = debugDao.getScheduleByDoctorId(doctorId, null, null, null, null).stream()
                .map(dtoAdapters::transform)
                .collect(Collectors.toList());

        return new GetScheduleByDoctorIdDtoResponse(responseSchedule);
    }
}