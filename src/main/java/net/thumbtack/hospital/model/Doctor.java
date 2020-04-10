package net.thumbtack.hospital.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO Добавить валидацию через аннотации
public class Doctor extends User {
    private String cabinetName;
    private String doctorSpecialtyName;
    private List<ScheduleCell> schedule;

    public Doctor() {
    }

    public Doctor(int id,
                  String login, String password,
                  String firstName, String lastName, String patronymic,
                  String cabinetName, String doctorSpecialtyName, List<ScheduleCell> schedule) {
        super(id, login, password, firstName, lastName, patronymic);

        setCabinetName(cabinetName);
        setDoctorSpecialtyName(doctorSpecialtyName);
        setSchedule(schedule);
    }

    public Doctor(int id,
                  String login, String password,
                  String firstName, String lastName, String patronymic,
                  String cabinetName, String doctorSpecialtyName) {
        this(id, login, password, firstName, lastName, patronymic, cabinetName, doctorSpecialtyName, new ArrayList<>());
    }

    public Doctor(String login, String password,
                  String firstName, String lastName, String patronymic,
                  String cabinetName, String doctorSpecialtyName) {
        this(0, login, password, firstName, lastName, patronymic, cabinetName, doctorSpecialtyName);
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public void setDoctorSpecialtyName(String doctorSpecialtyName) {
        this.doctorSpecialtyName = doctorSpecialtyName;
    }

    public void setSchedule(List<ScheduleCell> schedule) {
        this.schedule = schedule;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public String getDoctorSpecialtyName() {
        return doctorSpecialtyName;
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
        return Objects.equals(getCabinetName(), doctor.getCabinetName()) &&
                Objects.equals(getDoctorSpecialtyName(), doctor.getDoctorSpecialtyName()) &&
                Objects.equals(getSchedule(), doctor.getSchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCabinetName(), getDoctorSpecialtyName(), getSchedule());
    }

    @Override
    public String toString() {
        return super.toString() + "\nDoctor{" +
                "cabinetName='" + cabinetName + '\'' +
                ", doctorSpecialtyName='" + doctorSpecialtyName + '\'' +
                ", schedule=" + schedule +
                '}';
    }
}