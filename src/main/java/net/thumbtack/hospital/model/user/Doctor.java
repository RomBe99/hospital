package net.thumbtack.hospital.model.user;

import net.thumbtack.hospital.model.schedule.ScheduleCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Doctor extends User {
    private String cabinet;
    private String specialty;
    private List<ScheduleCell> schedule = new ArrayList<>();

    public Doctor() {
    }

    public Doctor(int id,
                  String login, String password,
                  String firstName, String lastName, String patronymic,
                  String cabinet, String specialty, List<ScheduleCell> schedule) {
        super(id, login, password, firstName, lastName, patronymic);

        setCabinet(cabinet);
        setSpecialty(specialty);
        setSchedule(schedule);
    }

    public Doctor(String login, String password,
                  String firstName, String lastName, String patronymic,
                  String cabinet, String specialty, List<ScheduleCell> schedule) {
        this(0, login, password, firstName, lastName, patronymic, cabinet, specialty, schedule);
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setSchedule(List<ScheduleCell> schedule) {
        this.schedule = schedule == null ? new ArrayList<>() : schedule;
    }

    public String getCabinet() {
        return cabinet;
    }

    public String getSpecialty() {
        return specialty;
    }

    public List<ScheduleCell> getSchedule() {
        return schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(getCabinet(), doctor.getCabinet()) &&
                Objects.equals(getSpecialty(), doctor.getSpecialty()) &&
                Objects.equals(getSchedule(), doctor.getSchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCabinet(), getSpecialty(), getSchedule());
    }

    @Override
    public String toString() {
        return super.toString() + " Doctor{" +
                "cabinetName='" + cabinet + '\'' +
                ", doctorSpecialtyName='" + specialty + '\'' +
                ", schedule=" + schedule +
                '}';
    }
}