package net.thumbtack.hospital.util;

import net.thumbtack.hospital.dtorequest.schedule.DayScheduleDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtorequest.schedule.WeekScheduleDtoRequest;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.util.adapter.DtoAdapters;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("ScheduleGenerator")
public class ScheduleGenerator {
    private final DtoAdapters dtoAdapters;

    @Autowired
    public ScheduleGenerator(DtoAdapters dtoAdapters) {
        this.dtoAdapters = dtoAdapters;
    }

    public DtoRequestWithSchedule generateDtoRequestWithWeekSchedule(int duration,
                                                                     LocalDate dateStart, LocalDate dateEnd,
                                                                     LocalTime timeStart, LocalTime timeEnd,
                                                                     List<Integer> weekDays) {
        var weekSchedule = new WeekScheduleDtoRequest(timeStart.toString(), timeEnd.toString(), weekDays);

        return new DtoRequestWithSchedule(dateStart.toString(), dateEnd.toString(), duration, weekSchedule) {
            @Override
            public void setDateStart(String dateStart) {
                super.setDateStart(dateStart);
            }

            @Override
            public void setDateEnd(String dateEnd) {
                super.setDateEnd(dateEnd);
            }

            @Override
            public void setDuration(int duration) {
                super.setDuration(duration);
            }

            @Override
            public void setWeekSchedule(WeekScheduleDtoRequest weekSchedule) {
                super.setWeekSchedule(weekSchedule);
            }

            @Override
            public void setWeekDaysSchedule(List<DayScheduleDtoRequest> weekDaysSchedule) {
                super.setWeekDaysSchedule(weekDaysSchedule);
            }

            @Override
            public String getDateStart() {
                return super.getDateStart();
            }

            @Override
            public String getDateEnd() {
                return super.getDateEnd();
            }

            @Override
            public int getDuration() {
                return super.getDuration();
            }

            @Override
            public WeekScheduleDtoRequest getWeekSchedule() {
                return super.getWeekSchedule();
            }

            @Override
            public List<DayScheduleDtoRequest> getWeekDaysSchedule() {
                return super.getWeekDaysSchedule();
            }

            @Override
            public boolean equals(Object o) {
                return super.equals(o);
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    public DtoRequestWithSchedule generateDtoRequestWithDaySchedule(int duration,
                                                                    LocalDate dateStart, LocalDate dateEnd,
                                                                    Map<WeekDay, Pair<LocalTime, LocalTime>> schedule) {
        final var weekDaySchedule = new ArrayList<DayScheduleDtoRequest>(schedule.size());

        for (var weekDay : schedule.keySet()) {
            final var temp = schedule.get(weekDay);
            weekDaySchedule.add(new DayScheduleDtoRequest(temp.getKey().toString(), temp.getValue().toString(), weekDay.getName()));
        }

        return new DtoRequestWithSchedule(dateStart.toString(), dateEnd.toString(), duration, weekDaySchedule) {
            @Override
            public void setDateStart(String dateStart) {
                super.setDateStart(dateStart);
            }

            @Override
            public void setDateEnd(String dateEnd) {
                super.setDateEnd(dateEnd);
            }

            @Override
            public void setDuration(int duration) {
                super.setDuration(duration);
            }

            @Override
            public void setWeekSchedule(WeekScheduleDtoRequest weekSchedule) {
                super.setWeekSchedule(weekSchedule);
            }

            @Override
            public void setWeekDaysSchedule(List<DayScheduleDtoRequest> weekDaysSchedule) {
                super.setWeekDaysSchedule(weekDaysSchedule);
            }

            @Override
            public String getDateStart() {
                return super.getDateStart();
            }

            @Override
            public String getDateEnd() {
                return super.getDateEnd();
            }

            @Override
            public int getDuration() {
                return super.getDuration();
            }

            @Override
            public WeekScheduleDtoRequest getWeekSchedule() {
                return super.getWeekSchedule();
            }

            @Override
            public List<DayScheduleDtoRequest> getWeekDaysSchedule() {
                return super.getWeekDaysSchedule();
            }

            @Override
            public boolean equals(Object o) {
                return super.equals(o);
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    public List<ScheduleCell> generateSchedule(int doctorId, DtoRequestWithSchedule requestWithSchedule) {
        return dtoAdapters.transform(requestWithSchedule, doctorId);
    }
}