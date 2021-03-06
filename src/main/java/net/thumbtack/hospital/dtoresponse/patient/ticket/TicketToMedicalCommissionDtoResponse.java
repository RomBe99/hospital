package net.thumbtack.hospital.dtoresponse.patient.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class TicketToMedicalCommissionDtoResponse extends TicketDtoResponse {
    private int patientId;
    private List<Integer> doctorIds;
    private int duration;

    public TicketToMedicalCommissionDtoResponse(String ticket,
                                                String room, LocalDate date, LocalTime time,
                                                int patientId, List<Integer> doctorIds, int duration) {
        super(ticket, room, date, time);

        setPatientId(patientId);
        setDoctorIds(doctorIds);
        setDuration(duration);
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
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TicketToMedicalCommissionDtoResponse that = (TicketToMedicalCommissionDtoResponse) o;
        return patientId == that.patientId &&
                duration == that.duration &&
                Objects.equals(doctorIds, that.doctorIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), patientId, doctorIds, duration);
    }

    @Override
    public String toString() {
        return super.toString() + " TicketToMedicalCommissionDtoResponse{" +
                "patientId=" + patientId +
                ", doctorIds=" + doctorIds +
                ", duration=" + duration +
                '}';
    }
}