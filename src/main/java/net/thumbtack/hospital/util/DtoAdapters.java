package net.thumbtack.hospital.util;

import net.thumbtack.hospital.dtorequest.other.schedulecell.WeekDayScheduleCellDtoRequest;
import net.thumbtack.hospital.dtorequest.other.schedulecell.WeekScheduleCellDtoRequest;
import net.thumbtack.hospital.dtoresponse.other.schedulecell.ScheduleCellResponse;
import net.thumbtack.hospital.dtoresponse.other.schedulecell.ScheduleTimeCellResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.model.Patient;
import net.thumbtack.hospital.model.ScheduleCell;
import net.thumbtack.hospital.model.TimeCell;

import java.util.List;
import java.util.stream.Collectors;

public class DtoAdapters {
    public static ScheduleTimeCellResponse transform(TimeCell tc) {
        return new ScheduleTimeCellResponse(tc.getTime().toString(),
                transform(tc.getPatient()),
                tc.getDuration());
    }

    public static ScheduleCellResponse transform(ScheduleCell sc) {
        return new ScheduleCellResponse(sc.getDate().toString(),
                sc.getCells().stream()
                        .map(DtoAdapters::transform)
                        .collect(Collectors.toList()));
    }

    public static PatientInformationDtoResponse transform(Patient patient) {
        return new PatientInformationDtoResponse(patient.getId(),
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }

    @Deprecated
    public static List<ScheduleCell> transformWSC(List<WeekScheduleCellDtoRequest> sc) {
        return null;
    }

    @Deprecated
    public static List<ScheduleCell> transformWDSC(List<WeekDayScheduleCellDtoRequest> sc) {
        return null;
    }
}