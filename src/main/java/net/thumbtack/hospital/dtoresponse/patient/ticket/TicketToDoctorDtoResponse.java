package net.thumbtack.hospital.dtoresponse.patient.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TicketToDoctorDtoResponse extends TicketDtoResponse {
    private int doctorId;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorPatronymic;
    private String speciality;

    public TicketToDoctorDtoResponse(String ticket, String room, LocalDate date, LocalTime time,
                                     int doctorId, String doctorFirstName, String doctorLastName, String doctorPatronymic,
                                     String speciality) {
        super(ticket, room, date, time);

        setDoctorId(doctorId);
        setDoctorFirstName(doctorFirstName);
        setDoctorLastName(doctorLastName);
        setDoctorPatronymic(doctorPatronymic);
        setSpeciality(speciality);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TicketToDoctorDtoResponse that = (TicketToDoctorDtoResponse) o;
        return doctorId == that.doctorId &&
                Objects.equals(doctorFirstName, that.doctorFirstName) &&
                Objects.equals(doctorLastName, that.doctorLastName) &&
                Objects.equals(doctorPatronymic, that.doctorPatronymic) &&
                Objects.equals(speciality, that.speciality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), doctorId, doctorFirstName, doctorLastName, doctorPatronymic, speciality);
    }

    @Override
    public String toString() {
        return super.toString() + " TicketToDoctorDtoResponse{" +
                "doctorId=" + doctorId +
                ", firstName='" + doctorFirstName + '\'' +
                ", lastName='" + doctorLastName + '\'' +
                ", patronymic='" + doctorPatronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}