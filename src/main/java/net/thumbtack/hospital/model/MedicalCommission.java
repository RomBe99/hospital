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
    private List<Doctor> doctors;

    public MedicalCommission() {
    }

    public MedicalCommission(int id, LocalDate date, LocalTime time, int patientId, int duration, List<Doctor> doctors) {
        setId(id);
        setDate(date);
        setTime(time);
        setPatientId(patientId);
        setDuration(duration);
        setDoctors(doctors);
    }

    public MedicalCommission(LocalDate date, LocalTime time, int patientId, int duration, List<Doctor> doctors) {
        this(0, date, time, patientId, duration, doctors);
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

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
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

    public List<Doctor> getDoctors() {
        return doctors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalCommission that = (MedicalCommission) o;
        return id == that.id &&
                patientId == that.patientId &&
                duration == that.duration &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time) &&
                Objects.equals(doctors, that.doctors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, time, patientId, duration, doctors);
    }

    @Override
    public String toString() {
        return "MedicalCommission{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", patientId=" + patientId +
                ", duration=" + duration +
                ", doctorIds=" + doctors +
                '}';
    }
}