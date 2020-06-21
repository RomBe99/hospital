package net.thumbtack.hospital.dtoresponse.patient.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TicketToDoctorDtoResponse extends TicketDtoResponse {
    private int doctorId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;

    public TicketToDoctorDtoResponse(String ticket, String room, LocalDate date, LocalTime time,
                                     int doctorId, String firstName, String lastName, String patronymic, String speciality) {
        super(ticket, room, date, time);

        setDoctorId(doctorId);
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setSpeciality(speciality);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TicketToDoctorDtoResponse that = (TicketToDoctorDtoResponse) o;
        return doctorId == that.doctorId &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(speciality, that.speciality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), doctorId, firstName, lastName, patronymic, speciality);
    }

    @Override
    public String toString() {
        return super.toString() + " TicketToDoctorDtoResponse{" +
                "doctorId=" + doctorId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}