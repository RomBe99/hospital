package net.thumbtack.hospital.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO Добавить валидацию через аннотации
public class ScheduleCell {
    private int id;
    private int doctorId;
    private LocalDate date;
    private List<TimeCell> cells;

    public ScheduleCell() {
    }

    public ScheduleCell(int id, int doctorId, LocalDate date, List<TimeCell> cells) {
        setId(id);
        setDoctorId(doctorId);
        setDate(date);
        setCells(cells);
    }

    public ScheduleCell(int id, int doctorId, LocalDate date) {
        this(id, doctorId, date, new ArrayList<>());
    }

    public ScheduleCell(int doctorId, LocalDate date) {
        this(0, doctorId, date);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCells(List<TimeCell> cells) {
        this.cells = cells;
    }

    public int getId() {
        return id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<TimeCell> getCells() {
        return cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleCell)) return false;
        ScheduleCell that = (ScheduleCell) o;
        return getId() == that.getId() &&
                getDoctorId() == that.getDoctorId() &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getCells(), that.getCells());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDoctorId(), getDate(), getCells());
    }

    @Override
    public String toString() {
        return "ScheduleCell{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", date=" + date +
                ", cells=" + cells +
                '}';
    }
}