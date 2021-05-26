package net.thumbtack.hospital.util.adapter;

import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleTimeCellDtoResponse;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.model.user.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component("DtoAdapters")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DtoAdapters {
    private final ScheduleTransformer scheduleTransformer;

    @Autowired
    public DtoAdapters(ScheduleTransformer scheduleTransformer) {
        this.scheduleTransformer = scheduleTransformer;
    }

    public ScheduleTimeCellDtoResponse transform(TimeCell tc) {
        return new ScheduleTimeCellDtoResponse(tc.getTime(),
                tc.getPatient() == null ? null : transform(tc.getPatient()),
                tc.getDuration());
    }

    public ScheduleCellDtoResponse transform(ScheduleCell sc) {
        return new ScheduleCellDtoResponse(sc.getDate(),
                sc.getCells() == null ? null : sc.getCells().stream()
                        .map(this::transform)
                        .collect(Collectors.toList()));
    }

    public List<ScheduleCellDtoResponse> transform(List<ScheduleCell> schedule) {
        return schedule.stream()
                .map(this::transform)
                .collect(Collectors.toList());
    }

    public PatientInformationDtoResponse transform(Patient patient) {
        return new PatientInformationDtoResponse(patient.getId(),
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }

    public List<ScheduleCell> transform(DtoRequestWithSchedule request, int doctorId) {
        if (scheduleTransformer.isEmptyScheduleRequest(request)) {
            return Collections.emptyList();
        }

        final var transformers = Map.<Supplier<Boolean>, BiFunction<DtoRequestWithSchedule, Integer, List<ScheduleCell>>>of(
                () -> request.getWeekSchedule() != null, scheduleTransformer::transformWeekSchedule,
                () -> !request.getWeekDaysSchedule().isEmpty(), scheduleTransformer::transformWeekDaysSchedule
        );

        for (var p : transformers.keySet()) {
            if (p.get()) {
                return scheduleTransformer.sortSchedule(transformers.get(p).apply(request, doctorId));
            }
        }

        return Collections.emptyList();
    }
}