package net.thumbtack.hospital.util;

import net.thumbtack.hospital.dtorequest.schedule.DayScheduleDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleTimeCellDtoResponse;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
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

    public static PatientInformationDtoResponse transform(Patient patient) {
        return new PatientInformationDtoResponse(patient.getId(),
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }

    public static List<ScheduleCell> transform(DtoRequestWithSchedule request, int doctorId) {
        int duration = request.getDuration();
        LocalDate dateStart = LocalDate.parse(request.getDateStart());
        LocalDate dateEnd = LocalDate.parse(request.getDateEnd());

        final Predicate<LocalDate> weekendChecker = d -> {
            DayOfWeek dayOfWeek = d.getDayOfWeek();

            return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
        };

        List<ScheduleCell> result = new ArrayList<>();

        if (request.getWeekSchedule() != null) {
            LocalTime durationStartTime = LocalTime.parse(request.getWeekSchedule().getTimeStart());
            LocalTime durationEndTime = LocalTime.parse(request.getWeekSchedule().getTimeEnd());

            List<LocalTime> durations = new ArrayList<>();

            for (LocalTime t = durationStartTime; t.isBefore(durationEndTime); t = t.plusMinutes(duration)) {
                durations.add(t);
            }

            List<TimeCell> temp;

            for (LocalDate d = dateStart; d.isBefore(dateEnd); d = d.plusDays(1)) {
                if (weekendChecker.test(d)) {
                    continue;
                }

                temp = new ArrayList<>();

                for (LocalTime t : durations) {
                    temp.add(new TimeCell(t, duration, TicketFactory.buildTicketToDoctor(doctorId, d, t)));
                }

                result.add(new ScheduleCell(null, d, temp));
            }
        }

        if (!request.getWeekDaysSchedule().isEmpty()) {
            List<DayScheduleDtoRequest> daySchedule = request.getWeekDaysSchedule();
            MultiValueMap<DayOfWeek, LocalTime> weekDurationTimes = new LinkedMultiValueMap<>();

            LocalTime durationStartTime;
            LocalTime durationEndTime;

            for (DayScheduleDtoRequest r : daySchedule) {
                durationStartTime = LocalTime.parse(r.getTimeStart());
                durationEndTime = LocalTime.parse(r.getTimeEnd());

                for (LocalTime t = durationStartTime; t.isBefore(durationEndTime); t = t.plusMinutes(duration)) {
                    weekDurationTimes.add(WeekDay.transformToDayOfWeek(r.getWeekDay()), t);
                }
            }

            List<LocalTime> durations;
            List<TimeCell> temp;

            for (LocalDate d = dateStart; d.isBefore(dateEnd); d = d.plusDays(1)) {
                durations = weekDurationTimes.get(d.getDayOfWeek());

                if (weekendChecker.test(d) || durations == null) {
                    continue;
                }

                temp = new ArrayList<>();

                for (LocalTime t : durations) {
                    temp.add(new TimeCell(t, duration, TicketFactory.buildTicketToDoctor(doctorId, d, t)));
                }

                result.add(new ScheduleCell(null, d, temp));
            }
        }

        return result;
    }
}