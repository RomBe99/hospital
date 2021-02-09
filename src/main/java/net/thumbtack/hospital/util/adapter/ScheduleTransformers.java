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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ScheduleTransformers {
    public static boolean isWeekend(LocalDate testDate, List<DayOfWeek> workDaysOfWeek) {
        DayOfWeek dayOfWeek = DayOfWeek.from(testDate);

        List<Predicate<DayOfWeek>> weekendDayCheckers = new ArrayList<>();
        weekendDayCheckers.add(d -> d == DayOfWeek.SATURDAY);
        weekendDayCheckers.add(d -> d == DayOfWeek.SUNDAY);
        weekendDayCheckers.add(d -> !workDaysOfWeek.contains(d));

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
        int duration = request.getDuration();
        LocalDate dateStart = LocalDate.parse(request.getDateStart());
        LocalDate dateEnd = LocalDate.parse(request.getDateEnd());
        List<DayOfWeek> workDaysOfWeek = request.getWeekSchedule().getWeekDays().stream()
                .map(DayOfWeek::of)
                .collect(Collectors.toList());

        LocalTime durationStartTime = LocalTime.parse(request.getWeekSchedule().getTimeStart());
        LocalTime durationEndTime = LocalTime.parse(request.getWeekSchedule().getTimeEnd());

        List<ScheduleCell> result = new LinkedList<>();
        List<LocalTime> durations = new LinkedList<>();

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
        int duration = request.getDuration();
        LocalDate dateStart = LocalDate.parse(request.getDateStart());
        LocalDate dateEnd = LocalDate.parse(request.getDateEnd());

        List<DayScheduleDtoRequest> daySchedule = request.getWeekDaysSchedule();
        MultiValueMap<DayOfWeek, LocalTime> weekDurationTimes = new LinkedMultiValueMap<>();

        List<ScheduleCell> result = new LinkedList<>();
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
        List<DayOfWeek> workDaysOfWeek = new ArrayList<>(weekDurationTimes.keySet());

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