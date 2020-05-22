package net.thumbtack.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class MedicalCommission {
    private int id;
    private LocalDate date;
    private LocalTime time;
    private int patientId;
    private int duration;
    private List<Integer> doctorIds;

    public MedicalCommission() {
    }

    public MedicalCommission(int id, LocalDate date, LocalTime time, int patientId, int duration, List<Integer> doctorIds) {
        setId(id);
        setDate(date);
        setTime(time);
        setPatientId(patientId);
        setDuration(duration);
        setDoctorIds(doctorIds);
    }

    public MedicalCommission(LocalDate date, LocalTime time, int patientId, int duration, List<Integer> doctorIds) {
        this(0, date, time, patientId, duration, doctorIds);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDoctorIds(List<Integer> doctorIds) {
        this.doctorIds = doctorIds;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDuration() {
        return duration;
    }

    public List<Integer> getDoctorIds() {
        return doctorIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicalCommission)) return false;
        MedicalCommission that = (MedicalCommission) o;
        return getId() == that.getId() &&
                getPatientId() == that.getPatientId() &&
                getDuration() == that.getDuration() &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getDoctorIds(), that.getDoctorIds());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getTime(), getPatientId(), getDuration(), getDoctorIds());
    }

    @Override
    public String toString() {
        return "MedicalCommission{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", patientId=" + patientId +
                ", duration=" + duration +
                ", doctorIds=" + doctorIds +
                '}';
    }
}