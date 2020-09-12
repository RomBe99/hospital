package net.thumbtack.hospital.model.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TicketToMedicalCommission extends Ticket {
    private int id;
    private int patientId;
    private List<Integer> doctorIds = new ArrayList<>();
    private int duration;

    public TicketToMedicalCommission() {
    }

    public TicketToMedicalCommission(int id, String title, String room, LocalDate date, LocalTime time,
                                     int patientId, List<Integer> doctorIds, int duration) {
        super(title, room, date, time);

        setId(id);
        setPatientId(patientId);
        setDoctorIds(doctorIds);
        setDuration(duration);
    }

    public TicketToMedicalCommission(String title, String room, LocalDate date, LocalTime time,
                                     int patientId, List<Integer> doctorIds, int duration) {
        this(0, title, room, date, time, patientId, doctorIds, duration);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDoctorIds(List<Integer> doctorIds) {
        this.doctorIds = doctorIds;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public List<Integer> getDoctorIds() {
        return doctorIds;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketToMedicalCommission)) return false;
        if (!super.equals(o)) return false;
        TicketToMedicalCommission that = (TicketToMedicalCommission) o;
        return getId() == that.getId() &&
                getPatientId() == that.getPatientId() &&
                getDuration() == that.getDuration() &&
                Objects.equals(getDoctorIds(), that.getDoctorIds());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getPatientId(), getDoctorIds(), getDuration());
    }

    @Override
    public String toString() {
        return super.toString() + " TicketToMedicalCommission{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", doctorIds=" + doctorIds +
                ", duration=" + duration +
                '}';
    }
}