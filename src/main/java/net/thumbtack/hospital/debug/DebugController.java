package net.thumbtack.hospital.debug;

import net.thumbtack.hospital.debug.dtoresponse.schedule.GetScheduleByDoctorIdDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.DtoResponseWithSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController("DebugController")
@RequestMapping(DebugController.PREFIX_URL)
public class DebugController {
    public static final String PREFIX_URL = "/api/debug";
    public static final String DEBUG_CLEAR_URL = "clear";
    public static final String GET_SCHEDULE_BY_DOCTOR_ID_URL = "schedule/{doctorId}";

    private final DebugService debugService;

    @Autowired
    public DebugController(DebugService debugService) {
        this.debugService = debugService;
    }

    @PostMapping(value = DEBUG_CLEAR_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EmptyDtoResponse clear() {
        debugService.clear();

        return new EmptyDtoResponse();
    }

    @GetMapping(value = GET_SCHEDULE_BY_DOCTOR_ID_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GetScheduleByDoctorIdDtoResponse getScheduleByDoctorId(@PathVariable int doctorId) {
        return debugService.getScheduleByDoctorId(doctorId);
    }
}