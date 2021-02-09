package net.thumbtack.hospital.dtoresponse.patient;

import java.util.Objects;

public class AppointmentToDoctorDtoResponse {
    private String ticket;
    private int doctorId;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorPatronymic;
    private String speciality;
    private String room;
    private String date;
    private String time;

    public AppointmentToDoctorDtoResponse() {
    }

    public AppointmentToDoctorDtoResponse(String ticket, int doctorId,
                                          String doctorFirstName, String doctorLastName, String doctorPatronymic,
                                          String speciality, String room, String date, String time) {
        setTicket(ticket);
        setDoctorId(doctorId);
        setDoctorFirstName(doctorFirstName);
        setDoctorLastName(doctorLastName);
        setDoctorPatronymic(doctorPatronymic);
        setSpeciality(speciality);
        setRoom(room);
        setDate(date);
        setTime(time);
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setDoctorFirstName(String doctorFirstName) {
        this.doctorFirstName = doctorFirstName;
    }

    public void setDoctorLastName(String doctorLastName) {
        this.doctorLastName = doctorLastName;
    }

    public void setDoctorPatronymic(String doctorPatronymic) {
        this.doctorPatronymic = doctorPatronymic;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTicket() {
        return ticket;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getDoctorFirstName() {
        return doctorFirstName;
    }

    public String getDoctorLastName() {
        return doctorLastName;
    }

    public String getDoctorPatronymic() {
        return doctorPatronymic;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getRoom() {
        return room;
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
        AppointmentToDoctorDtoResponse that = (AppointmentToDoctorDtoResponse) o;
        return doctorId == that.doctorId &&
                Objects.equals(ticket, that.ticket) &&
                Objects.equals(doctorFirstName, that.doctorFirstName) &&
                Objects.equals(doctorLastName, that.doctorLastName) &&
                Objects.equals(doctorPatronymic, that.doctorPatronymic) &&
                Objects.equals(speciality, that.speciality) &&
                Objects.equals(room, that.room) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket, doctorId, doctorFirstName, doctorLastName, doctorPatronymic, speciality, room, date, time);
    }

    @Override
    public String toString() {
        return "AppointmentToDoctorDtoResponse{" +
                "ticket='" + ticket + '\'' +
                ", doctorId=" + doctorId +
                ", doctorFirstName='" + doctorFirstName + '\'' +
                ", doctorLastName='" + doctorLastName + '\'' +
                ", doctorPatronymic='" + doctorPatronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}