package net.thumbtack.hospital.util.adapter;

import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.util.WeekDay;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component("ScheduleTransformer")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ScheduleTransformer {
    private final TicketFactory ticketFactory;

    @Autowired
    public ScheduleTransformer(TicketFactory ticketFactory) {
        this.ticketFactory = ticketFactory;
    }

    public boolean isWeekend(LocalDate testDate, List<DayOfWeek> workDaysOfWeek) {
        final var dayOfWeek = DayOfWeek.from(testDate);

        final var weekendDayCheckers = List.<Predicate<DayOfWeek>>of(
                d -> d == DayOfWeek.SATURDAY,
                d -> d == DayOfWeek.SUNDAY,
                d -> !workDaysOfWeek.contains(d)
        );

        for (var c : weekendDayCheckers) {
            if (c.test(dayOfWeek)) {
                return true;
            }
        }

        return false;
    }

    public boolean isEmptyScheduleRequest(DtoRequestWithSchedule request) {
        return request.getWeekSchedule() == null && request.getWeekDaysSchedule().isEmpty();
    }

    public List<ScheduleCell> transformWeekSchedule(DtoRequestWithSchedule request, int doctorId) {
        final var duration = request.getDuration();
        final var dateStart = LocalDate.parse(request.getDateStart());
        final var dateEnd = LocalDate.parse(request.getDateEnd());
        final var workDaysOfWeek = request.getWeekSchedule().getWeekDays().stream()
                .map(DayOfWeek::of)
                .collect(Collectors.toList());

        final var durationStartTime = LocalTime.parse(request.getWeekSchedule().getTimeStart());
        final var durationEndTime = LocalTime.parse(request.getWeekSchedule().getTimeEnd());

        final var result = new LinkedList<ScheduleCell>();
        final var durations = new LinkedList<LocalTime>();

        for (var t = durationStartTime; t.isBefore(durationEndTime); t = t.plusMinutes(duration)) {
            durations.add(t);
        }

        for (var d = dateStart; d.isBefore(dateEnd); d = d.plusDays(1)) {
            if (isWeekend(d, workDaysOfWeek)) {
                continue;
            }

            final var temp = new ArrayList<TimeCell>();

            for (var t : durations) {
                temp.add(new TimeCell(t, duration, ticketFactory.buildTicketToDoctor(doctorId, d, t)));
            }

            result.add(new ScheduleCell(null, d, temp));
        }

        return result;
    }

    public List<ScheduleCell> transformWeekDaysSchedule(DtoRequestWithSchedule request, int doctorId) {
        final var duration = request.getDuration();
        final var dateStart = LocalDate.parse(request.getDateStart());
        final var dateEnd = LocalDate.parse(request.getDateEnd());

        final var daySchedule = request.getWeekDaysSchedule();
        final var weekDurationTimes = new LinkedMultiValueMap<DayOfWeek, LocalTime>();

        final var result = new LinkedList<ScheduleCell>();

        for (var r : daySchedule) {
            final var durationStartTime = LocalTime.parse(r.getTimeStart());
            final var durationEndTime = LocalTime.parse(r.getTimeEnd());

            for (var t = durationStartTime; t.isBefore(durationEndTime); t = t.plusMinutes(duration)) {
                weekDurationTimes.add(WeekDay.transformToDayOfWeek(r.getWeekDay()), t);
            }
        }

        final var workDaysOfWeek = new ArrayList<>(weekDurationTimes.keySet());

        for (var d = dateStart; d.isBefore(dateEnd); d = d.plusDays(1)) {
            final var durations = weekDurationTimes.get(d.getDayOfWeek());

            if (isWeekend(d, workDaysOfWeek) || durations == null) {
                continue;
            }

            final var temp = new ArrayList<TimeCell>();

            for (var t : durations) {
                temp.add(new TimeCell(t, duration, ticketFactory.buildTicketToDoctor(doctorId, d, t)));
            }

            result.add(new ScheduleCell(null, d, temp));
        }

        return result;
    }

    public List<ScheduleCell> sortSchedule(List<ScheduleCell> schedule) {
        schedule.sort(Comparator.comparing(ScheduleCell::getDate));
        schedule.forEach(sc -> sc.getCells().sort(Comparator.comparing(TimeCell::getTime)));

        return schedule;
    }
}