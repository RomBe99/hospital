package net.thumbtack.hospital.model.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TicketToDoctor extends Ticket {
    private int doctorId;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorPatronymic;
    private String speciality;

    public TicketToDoctor() {
    }

    public TicketToDoctor(String ticket, String room, LocalDate date, LocalTime time,
                          int doctorId, String doctorFirstName, String doctorLastName, String doctorPatronymic, String speciality) {
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
        if (!(o instanceof TicketToDoctor)) return false;
        if (!super.equals(o)) return false;
        TicketToDoctor that = (TicketToDoctor) o;
        return getDoctorId() == that.getDoctorId() &&
                Objects.equals(getDoctorFirstName(), that.getDoctorFirstName()) &&
                Objects.equals(getDoctorLastName(), that.getDoctorLastName()) &&
                Objects.equals(getDoctorPatronymic(), that.getDoctorPatronymic()) &&
                Objects.equals(getSpeciality(), that.getSpeciality());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDoctorId(), getDoctorFirstName(), getDoctorLastName(), getDoctorPatronymic(), getSpeciality());
    }

    @Override
    public String toString() {
        return super.toString() + " TicketToDoctor{" +
                "doctorId=" + doctorId +
                ", firstName='" + doctorFirstName + '\'' +
                ", lastName='" + doctorLastName + '\'' +
                ", patronymic='" + doctorPatronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}