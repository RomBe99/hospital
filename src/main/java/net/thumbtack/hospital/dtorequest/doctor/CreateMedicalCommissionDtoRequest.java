package net.thumbtack.hospital.dtorequest.doctor;

import java.util.List;
import java.util.Objects;

public class CreateMedicalCommissionDtoRequest {
    private int patientId;
    private List<Integer> doctorIds;
    private String room;
    private String date;
    private String time;
    private int duration;

    public CreateMedicalCommissionDtoRequest() {
    }

    public CreateMedicalCommissionDtoRequest(int patientId, List<Integer> doctorIds,
                                             String room, String date, String time, int duration) {
        setPatientId(patientId);
        setDoctorIds(doctorIds);
        setRoom(room);
        setDate(date);
        setTime(time);
        setDuration(duration);
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDoctorIds(List<Integer> doctorIds) {
        this.doctorIds = doctorIds;
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

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPatientId() {
        return patientId;
    }

    public List<Integer> getDoctorIds() {
        return doctorIds;
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

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateMedicalCommissionDtoRequest that = (CreateMedicalCommissionDtoRequest) o;
        return patientId == that.patientId &&
                duration == that.duration &&
                Objects.equals(doctorIds, that.doctorIds) &&
                Objects.equals(room, that.room) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, doctorIds, room, date, time, duration);
    }

    @Override
    public String toString() {
        return "CreateMedicalCommissionDtoRequest{" +
                "patientId=" + patientId +
                ", doctorIds=" + doctorIds +
                ", room='" + room + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", duration=" + duration +
                '}';
    }
}