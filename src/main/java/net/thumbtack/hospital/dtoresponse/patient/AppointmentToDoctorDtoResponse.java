package net.thumbtack.hospital.dtoresponse.patient;

import java.util.Objects;

public class AppointmentToDoctorDtoResponse {
    private String ticket;
    private int doctorId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;
    private String room;
    private String date;
    private String time;

    public AppointmentToDoctorDtoResponse() {
    }

    public AppointmentToDoctorDtoResponse(String ticket, int doctorId,
                                          String firstName, String lastName, String patronymic,
                                          String speciality, String room, String date, String time) {
        setTicket(ticket);
        setDoctorId(doctorId);
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setSpeciality(speciality);
        setRoom(room);
        setDate(date);
        setTime(time);
    }

    public AppointmentToDoctorDtoResponse(String ticket, int doctorId,
                                          String firstName, String lastName,
                                          String speciality, String room, String date, String time) {
        this(ticket, doctorId, firstName, lastName, null, speciality, room, date, time);
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
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
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(speciality, that.speciality) &&
                Objects.equals(room, that.room) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket, doctorId, firstName, lastName, patronymic, speciality, room, date, time);
    }

    @Override
    public String toString() {
        return "AppointmentToDoctorDtoResponse{" +
                "ticket='" + ticket + '\'' +
                ", doctorId=" + doctorId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}