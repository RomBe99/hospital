package net.thumbtack.hospital.util.adapter;

import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleTimeCellDtoResponse;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.model.user.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DtoAdapters {
    public static ScheduleTimeCellDtoResponse transform(TimeCell tc) {
        return new ScheduleTimeCellDtoResponse(tc.getTime(),
                tc.getPatient() == null ? null : transform(tc.getPatient()),
                tc.getDuration());
    }

    public static ScheduleCellDtoResponse transform(ScheduleCell sc) {
        return new ScheduleCellDtoResponse(sc.getDate(),
                sc.getCells() == null ? null : sc.getCells().stream()
                        .map(DtoAdapters::transform)
                        .collect(Collectors.toList()));
    }

    public static List<ScheduleCellDtoResponse> transform(List<ScheduleCell> schedule) {
        return schedule.stream()
                .map(DtoAdapters::transform)
                .collect(Collectors.toList());
    }

    public static PatientInformationDtoResponse transform(Patient patient) {
        return new PatientInformationDtoResponse(patient.getId(),
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }

    public static List<ScheduleCell> transform(DtoRequestWithSchedule request, int doctorId) {
        if (ScheduleTransformers.isEmptyScheduleRequest(request)) {
            return new ArrayList<>();
        }

        Map<Supplier<Boolean>, Supplier<List<ScheduleCell>>> transformers = new HashMap<>();
        transformers.put(() -> request.getWeekSchedule() != null,
                () -> ScheduleTransformers.transformWeekSchedule(request, doctorId));
        transformers.put(() -> !request.getWeekDaysSchedule().isEmpty(),
                () -> ScheduleTransformers.transformWeekDaysSchedule(request, doctorId));

        for (Supplier<Boolean> p : transformers.keySet()) {
            if (p.get()) {
                return ScheduleTransformers.sortSchedule(transformers.get(p).get());
            }
        }

        return new ArrayList<>();
    }
}