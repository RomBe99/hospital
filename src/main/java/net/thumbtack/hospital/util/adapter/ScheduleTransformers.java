package net.thumbtack.hospital.util.adapter;

import net.thumbtack.hospital.dtorequest.schedule.DayScheduleDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.util.WeekDay;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ScheduleTransformers {
    public static boolean isWeekend(LocalDate testDate, List<DayOfWeek> workDaysOfWeek) {
        DayOfWeek dayOfWeek = DayOfWeek.from(testDate);

        final List<Predicate<DayOfWeek>> weekendDayCheckers = Arrays.asList(
                d -> d == DayOfWeek.SATURDAY,
                d -> d == DayOfWeek.SUNDAY,
                d -> !workDaysOfWeek.contains(d)
        );

        for (Predicate<DayOfWeek> c : weekendDayCheckers) {
            if (c.test(dayOfWeek)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmptyScheduleRequest(DtoRequestWithSchedule request) {
        return request.getWeekSchedule() == null && request.getWeekDaysSchedule().isEmpty();
    }

    public static List<ScheduleCell> transformWeekSchedule(DtoRequestWithSchedule request, int doctorId) {
        final int duration = request.getDuration();
        final LocalDate dateStart = LocalDate.parse(request.getDateStart());
        final LocalDate dateEnd = LocalDate.parse(request.getDateEnd());
        final List<DayOfWeek> workDaysOfWeek = request.getWeekSchedule().getWeekDays().stream()
                .map(DayOfWeek::of)
                .collect(Collectors.toList());

        final LocalTime durationStartTime = LocalTime.parse(request.getWeekSchedule().getTimeStart());
        final LocalTime durationEndTime = LocalTime.parse(request.getWeekSchedule().getTimeEnd());

        final List<ScheduleCell> result = new LinkedList<>();
        final List<LocalTime> durations = new LinkedList<>();

        for (LocalTime t = durationStartTime; t.isBefore(durationEndTime); t = t.plusMinutes(duration)) {
            durations.add(t);
        }

        List<TimeCell> temp;

        for (LocalDate d = dateStart; d.isBefore(dateEnd); d = d.plusDays(1)) {
            if (isWeekend(d, workDaysOfWeek)) {
                continue;
            }

            temp = new ArrayList<>();

            for (LocalTime t : durations) {
                temp.add(new TimeCell(t, duration, TicketFactory.buildTicketToDoctor(doctorId, d, t)));
            }

            result.add(new ScheduleCell(null, d, temp));
        }

        return result;
    }

    public static List<ScheduleCell> transformWeekDaysSchedule(DtoRequestWithSchedule request, int doctorId) {
        final int duration = request.getDuration();
        final LocalDate dateStart = LocalDate.parse(request.getDateStart());
        final LocalDate dateEnd = LocalDate.parse(request.getDateEnd());

        final List<DayScheduleDtoRequest> daySchedule = request.getWeekDaysSchedule();
        final MultiValueMap<DayOfWeek, LocalTime> weekDurationTimes = new LinkedMultiValueMap<>();

        final List<ScheduleCell> result = new LinkedList<>();
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
        final List<DayOfWeek> workDaysOfWeek = new ArrayList<>(weekDurationTimes.keySet());

        for (LocalDate d = dateStart; d.isBefore(dateEnd); d = d.plusDays(1)) {
            durations = weekDurationTimes.get(d.getDayOfWeek());

            if (isWeekend(d, workDaysOfWeek) || durations == null) {
                continue;
            }

            temp = new ArrayList<>();

            for (LocalTime t : durations) {
                temp.add(new TimeCell(t, duration, TicketFactory.buildTicketToDoctor(doctorId, d, t)));
            }

            result.add(new ScheduleCell(null, d, temp));
        }

        return result;
    }

    public static List<ScheduleCell> sortSchedule(List<ScheduleCell> schedule) {
        schedule.sort(Comparator.comparing(ScheduleCell::getDate));
        schedule.forEach(sc -> sc.getCells().sort(Comparator.comparing(TimeCell::getTime)));

        return schedule;
    }
}