package net.thumbtack.hospital.dtoresponse.admin;

import net.thumbtack.hospital.dtoresponse.schedule.DtoResponseWithSchedule;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;

import java.util.List;
import java.util.Objects;

public class EditDoctorScheduleDtoResponse extends DtoResponseWithSchedule {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;
    private String room;

    public EditDoctorScheduleDtoResponse() {
    }

    public EditDoctorScheduleDtoResponse(int id,
                                         String firstName, String lastName, String patronymic,
                                         String speciality, String room,
                                         List<ScheduleCellDtoResponse> schedule) {
        super(schedule);

        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setSpeciality(speciality);
        setRoom(room);
    }

    public EditDoctorScheduleDtoResponse(String firstName, String lastName, String patronymic,
                                         String speciality, String room,
                                         List<ScheduleCellDtoResponse> schedule) {
        this(0, firstName, lastName, patronymic, speciality, room, schedule);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditDoctorScheduleDtoResponse response = (EditDoctorScheduleDtoResponse) o;
        return id == response.id &&
                Objects.equals(firstName, response.firstName) &&
                Objects.equals(lastName, response.lastName) &&
                Objects.equals(patronymic, response.patronymic) &&
                Objects.equals(speciality, response.speciality) &&
                Objects.equals(room, response.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, speciality, room);
    }

    @Override
    public String toString() {
        return super.toString() + " EditDoctorScheduleDtoResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                '}';
    }
}