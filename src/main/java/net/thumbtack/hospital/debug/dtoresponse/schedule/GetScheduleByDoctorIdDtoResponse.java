package net.thumbtack.hospital.debug.dtoresponse.schedule;

import net.thumbtack.hospital.dtoresponse.schedule.DtoResponseWithSchedule;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellResponse;

import java.util.List;

public class GetScheduleByDoctorIdDtoResponse extends DtoResponseWithSchedule {
    public GetScheduleByDoctorIdDtoResponse() {
    }

    public GetScheduleByDoctorIdDtoResponse(List<ScheduleCellResponse> schedule) {
        super(schedule);
    }
}