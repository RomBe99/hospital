package net.thumbtack.hospital.dtoresponse.user;

import net.thumbtack.hospital.dtoresponse.other.schedulecell.ScheduleCellResponse;

import java.util.List;
import java.util.Objects;

public class GetDoctorDtoResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;
    private String room;
    private List<ScheduleCellResponse> schedule;

    public GetDoctorDtoResponse() {
    }

    public GetDoctorDtoResponse(int id,
                                String firstName, String lastName, String patronymic,
                                String speciality, String room,
                                List<ScheduleCellResponse> schedule) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setSpeciality(speciality);
        setRoom(room);
        setSchedule(schedule);
    }

    public void setId(int id) {
        this.id = id;
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

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSchedule(List<ScheduleCellResponse> schedule) {
        this.schedule = schedule;
    }

    public int getId() {
        return id;
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
        GetDoctorDtoResponse that = (GetDoctorDtoResponse) o;
        return id == that.id &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(speciality, that.speciality) &&
                Objects.equals(room, that.room) &&
                Objects.equals(schedule, that.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, speciality, room, schedule);
    }

    @Override
    public String toString() {
        return "GetDoctorDtoResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                ", schedule=" + schedule +
                '}';
    }
}
