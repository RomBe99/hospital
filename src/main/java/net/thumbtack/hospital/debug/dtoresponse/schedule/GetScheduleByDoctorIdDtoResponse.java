package net.thumbtack.hospital.debug.dtoresponse.schedule;

import net.thumbtack.hospital.dtoresponse.schedule.DtoResponseWithSchedule;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;

import java.util.List;

public class GetScheduleByDoctorIdDtoResponse extends DtoResponseWithSchedule {
    public GetScheduleByDoctorIdDtoResponse() {
    }

    public GetScheduleByDoctorIdDtoResponse(List<ScheduleCellDtoResponse> schedule) {
        super(schedule);
    }
}