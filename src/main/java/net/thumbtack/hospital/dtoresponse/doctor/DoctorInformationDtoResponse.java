package net.thumbtack.hospital.dtoresponse.doctor;

import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.schedulecell.ScheduleCellResponse;

import java.util.List;
import java.util.Objects;

public class DoctorInformationDtoResponse extends UserInformationDtoResponse {
    private String speciality;
    private String room;
    private List<ScheduleCellResponse> schedule;

    public DoctorInformationDtoResponse() {
    }

    public DoctorInformationDtoResponse(int id,
                                        String login, String password,
                                        String firstName, String lastName, String patronymic,
                                        String speciality, String room,
                                        List<ScheduleCellResponse> schedule) {
        super(id, login, password, firstName, lastName, patronymic);

        setSpeciality(speciality);
        setRoom(room);
        setSchedule(schedule);
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSchedule(List<ScheduleCellResponse> schedule) {
        this.schedule = schedule;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getRoom() {
        return room;
    }

    public List<ScheduleCellResponse> getSchedule() {
        return schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DoctorInformationDtoResponse that = (DoctorInformationDtoResponse) o;
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
        return super.toString() + " DoctorInformationDtoResponse{" +
                "speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                ", schedule=" + schedule +
                '}';
    }
}