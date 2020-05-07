package net.thumbtack.hospital.dtoresponse.other.schedulecell;

import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;

import java.util.Objects;

public class ScheduleTimeCellResponse {
    private String time;
    private PatientInformationDtoResponse patient;
    private int duration;

    public ScheduleTimeCellResponse() {
    }

    public ScheduleTimeCellResponse(String time, PatientInformationDtoResponse patient, int duration) {
        setTime(time);
        setPatient(patient);
        setDuration(duration);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPatient(PatientInformationDtoResponse patient) {
        this.patient = patient;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public PatientInformationDtoResponse getPatient() {
        return patient;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleTimeCellResponse that = (ScheduleTimeCellResponse) o;
        return duration == that.duration &&
                Objects.equals(time, that.time) &&
                Objects.equals(patient, that.patient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, patient, duration);
    }

    @Override
    public String toString() {
        return "ScheduleTimeCellResponse{" +
                "time='" + time + '\'' +
                ", patient=" + patient +
                ", duration=" + duration +
                '}';
    }
}