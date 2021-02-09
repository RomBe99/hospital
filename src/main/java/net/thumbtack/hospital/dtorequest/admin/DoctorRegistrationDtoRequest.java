package net.thumbtack.hospital.dtorequest.admin;

import net.thumbtack.hospital.dtorequest.schedule.DayScheduleDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtorequest.schedule.WeekScheduleDtoRequest;
import net.thumbtack.hospital.util.validator.annotation.*;

import java.util.List;
import java.util.Objects;

public class DoctorRegistrationDtoRequest extends DtoRequestWithSchedule {
    @Name
    private String firstName;
    @Name
    private String lastName;
    @Name(isPatronymic = true)
    private String patronymic;
    @Speciality
    private String speciality;
    @Room
    private String room;
    @Login
    private String login;
    @Password
    private String password;

    public DoctorRegistrationDtoRequest() {
    }

    public DoctorRegistrationDtoRequest(String dateStart, String dateEnd, int duration, WeekScheduleDtoRequest weekSchedule,
                                        String firstName, String lastName, String patronymic,
                                        String speciality, String room,
                                        String login, String password) {
        super(dateStart, dateEnd, duration, weekSchedule);

        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setSpeciality(speciality);
        setRoom(room);
        setLogin(login);
        setPassword(password);
    }

    public DoctorRegistrationDtoRequest(String dateStart, String dateEnd, int duration, List<DayScheduleDtoRequest> weekDaysSchedule,
                                        String firstName, String lastName, String patronymic,
                                        String speciality, String room,
                                        String login, String password) {
        super(dateStart, dateEnd, duration, weekDaysSchedule);

        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setSpeciality(speciality);
        setRoom(room);
        setLogin(login);
        setPassword(password);
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

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DoctorRegistrationDtoRequest that = (DoctorRegistrationDtoRequest) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(speciality, that.speciality) &&
                Objects.equals(room, that.room) &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, patronymic, speciality, room, login, password);
    }

    @Override
    public String toString() {
        return super.toString() + " DoctorRegistrationDtoRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}