package net.thumbtack.hospital.model.schedule;

import net.thumbtack.hospital.model.user.Doctor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ScheduleCell {
    private int id;
    private Doctor doctor;
    private LocalDate date;
    private List<TimeCell> cells;

    public ScheduleCell() {
    }

    public ScheduleCell(int id, Doctor doctor, LocalDate date, List<TimeCell> cells) {
        setId(id);
        setDoctor(doctor);
        setDate(date);
        setCells(cells);
    }

    public ScheduleCell(Doctor doctor, LocalDate date, List<TimeCell> cells) {
        this(0, doctor, date, cells);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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

    public Doctor getDoctor() {
        return doctor;
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
                getDoctor() == that.getDoctor() &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getCells(), that.getCells());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDoctor(), getDate(), getCells());
    }

    @Override
    public String toString() {
        return "ScheduleCell{" +
                "id=" + id +
                ", doctorId=" + doctor +
                ", date=" + date +
                ", cells=" + cells +
                '}';
    }
}