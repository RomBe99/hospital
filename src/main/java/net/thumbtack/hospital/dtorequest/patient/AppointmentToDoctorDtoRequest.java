package net.thumbtack.hospital.dtorequest.patient;

import net.thumbtack.hospital.util.validator.annotation.AppointmentToDoctor;
import net.thumbtack.hospital.util.validator.annotation.Date;
import net.thumbtack.hospital.util.validator.annotation.Time;

import java.util.Objects;

@AppointmentToDoctor
public class AppointmentToDoctorDtoRequest {
    private int doctorId;
    private String speciality;
    @Date
    private String date;
    @Time
    private String time;

    public AppointmentToDoctorDtoRequest() {
    }

    private AppointmentToDoctorDtoRequest(int doctorId, String speciality, String date, String time) {
        setDoctorId(doctorId);
        setSpeciality(speciality);
        setDate(date);
        setTime(time);
    }

    public AppointmentToDoctorDtoRequest(int doctorId, String date, String time) {
        this(doctorId, null, date, time);
    }

    public AppointmentToDoctorDtoRequest(String speciality, String date, String time) {
        this(0, speciality, date, time);
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentToDoctorDtoRequest that = (AppointmentToDoctorDtoRequest) o;
        return doctorId == that.doctorId &&
                Objects.equals(speciality, that.speciality) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, speciality, date, time);
    }

    @Override
    public String toString() {
        return "AppointmentToDoctorDtoRequest{" +
                "doctorId=" + doctorId +
                ", speciality='" + speciality + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}