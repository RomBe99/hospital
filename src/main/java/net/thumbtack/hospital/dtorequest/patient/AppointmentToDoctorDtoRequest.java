package net.thumbtack.hospital.dtorequest.patient;

import java.util.Objects;

public class AppointmentToDoctorDtoRequest {
    private String doctorId;
    private String speciality;
    private String date;
    private String time;

    public AppointmentToDoctorDtoRequest() {
    }

    public AppointmentToDoctorDtoRequest(String doctorId, String speciality, String date, String time) {
        setDoctorId(doctorId);
        setSpeciality(speciality);
        setDate(date);
        setTime(time);
    }

    public void setDoctorId(String doctorId) {
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

    public String getDoctorId() {
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
        return Objects.equals(doctorId, that.doctorId) &&
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
                "doctorId='" + doctorId + '\'' +
                ", speciality='" + speciality + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}