package net.thumbtack.hospital.dtoresponse.doctor;

import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DoctorLoginDtoResponse extends LoginUserDtoResponse {
    private String speciality;
    private String room;
    private List<ScheduleCellDtoResponse> schedule = new ArrayList<>();

    public DoctorLoginDtoResponse() {
    }

    public DoctorLoginDtoResponse(int id,
                                  String firstName, String lastName, String patronymic,
                                  String speciality, String room, List<ScheduleCellDtoResponse> schedule) {
        super(id, firstName, lastName, patronymic);

        setSpeciality(speciality);
        setRoom(room);
        setSchedule(schedule);
    }

    public DoctorLoginDtoResponse(String firstName, String lastName, String patronymic,
                                  String speciality, String room, List<ScheduleCellDtoResponse> schedule) {
        this(0, firstName, lastName, patronymic, speciality, room, schedule);
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSchedule(List<ScheduleCellDtoResponse> schedule) {
        this.schedule = schedule;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getRoom() {
        return room;
    }

    public List<ScheduleCellDtoResponse> getSchedule() {
        return schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DoctorLoginDtoResponse that = (DoctorLoginDtoResponse) o;
        return Objects.equals(speciality, that.speciality) &&
                Objects.equals(room, that.room) &&
                Objects.equals(schedule, that.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), speciality, room, schedule);
    }

    @Override
    public String toString() {
        return super.toString() + " DoctorLoginDtoResponse{" +
                "speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                ", schedule=" + schedule +
                '}';
    }
}