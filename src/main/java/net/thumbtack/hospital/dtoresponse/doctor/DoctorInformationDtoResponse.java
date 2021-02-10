package net.thumbtack.hospital.dtoresponse.doctor;

import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DoctorInformationDtoResponse extends UserInformationDtoResponse {
    private String speciality;
    private String room;
    private List<ScheduleCellDtoResponse> schedule = new ArrayList<>();

    public DoctorInformationDtoResponse() {
    }

    public DoctorInformationDtoResponse(int id,
                                        String login, String password,
                                        String firstName, String lastName, String patronymic,
                                        String speciality, String room,
                                        List<ScheduleCellDtoResponse> schedule) {
        super(id, login, password, firstName, lastName, patronymic);

        setSpeciality(speciality);
        setRoom(room);
        setSchedule(schedule);
    }

    public DoctorInformationDtoResponse(String login, String password,
                                        String firstName, String lastName, String patronymic,
                                        String speciality, String room,
                                        List<ScheduleCellDtoResponse> schedule) {
        this(0, login, password, firstName, lastName, patronymic, speciality, room, schedule);
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSchedule(List<ScheduleCellDtoResponse> schedule) {
        this.schedule = schedule == null ? new ArrayList<>() : schedule;
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
        DoctorInformationDtoResponse response = (DoctorInformationDtoResponse) o;
        return Objects.equals(speciality, response.speciality) &&
                Objects.equals(room, response.room) &&
                Objects.equals(schedule, response.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), speciality, room, schedule);
    }

    @Override
    public String toString() {
        return super.toString() + " DoctorInformationDtoResponse{" +
                "speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                ", schedule=" + schedule +
                '}';
    }
}