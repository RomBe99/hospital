package net.thumbtack.hospital.dtorequest.admin;

import net.thumbtack.hospital.dtorequest.schedule.WeekDayScheduleCellDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.WeekScheduleCellDtoRequest;
import net.thumbtack.hospital.util.validator.annotation.*;

import java.util.List;
import java.util.Objects;

public class DoctorRegistrationDtoRequest {
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
    @Date
    private String dateStart;
    @Date
    private String dateEnd;
    private List<WeekScheduleCellDtoRequest> weekSchedule;
    private List<WeekDayScheduleCellDtoRequest> weekDaysSchedule;
    @Duration
    private int duration;

    public DoctorRegistrationDtoRequest() {
    }

    public DoctorRegistrationDtoRequest(String firstName, String lastName, String patronymic,
                                        String speciality, String room,
                                        String login, String password,
                                        String dateStart, String dateEnd,
                                        List<WeekScheduleCellDtoRequest> weekSchedule,
                                        List<WeekDayScheduleCellDtoRequest> weekDaysSchedule,
                                        int duration) {
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setSpeciality(speciality);
        setRoom(room);
        setLogin(login);
        setPassword(password);
        setDateStart(dateStart);
        setDateEnd(dateEnd);
        setWeekSchedule(weekSchedule);
        setWeekDaysSchedule(weekDaysSchedule);
        setDuration(duration);
    }

    public DoctorRegistrationDtoRequest(String firstName, String lastName,
                                        String speciality, String room,
                                        String login, String password,
                                        String dateStart, String dateEnd,
                                        List<WeekScheduleCellDtoRequest> weekSchedule,
                                        List<WeekDayScheduleCellDtoRequest> weekDaysSchedule,
                                        int duration) {
        this(firstName, lastName, null,
                speciality, room, login, password, dateStart, dateEnd, weekSchedule, weekDaysSchedule, duration);
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

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setWeekSchedule(List<WeekScheduleCellDtoRequest> weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public void setWeekDaysSchedule(List<WeekDayScheduleCellDtoRequest> weekDaysSchedule) {
        this.weekDaysSchedule = weekDaysSchedule;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public List<WeekScheduleCellDtoRequest> getWeekSchedule() {
        return weekSchedule;
    }

    public List<WeekDayScheduleCellDtoRequest> getWeekDaysSchedule() {
        return weekDaysSchedule;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorRegistrationDtoRequest that = (DoctorRegistrationDtoRequest) o;
        return duration == that.duration &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(speciality, that.speciality) &&
                Objects.equals(room, that.room) &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password) &&
                Objects.equals(dateStart, that.dateStart) &&
                Objects.equals(dateEnd, that.dateEnd) &&
                Objects.equals(weekSchedule, that.weekSchedule) &&
                Objects.equals(weekDaysSchedule, that.weekDaysSchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, speciality, room, login, password, dateStart, dateEnd, weekSchedule, weekDaysSchedule, duration);
    }

    @Override
    public String toString() {
        return "DoctorRegistrationDtoRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", weekSchedule=" + weekSchedule +
                ", weekDaysSchedule=" + weekDaysSchedule +
                ", duration=" + duration +
                '}';
    }
}