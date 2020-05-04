package net.thumbtack.hospital.dtorequest.admin;

import net.thumbtack.hospital.util.validator.annotation.Login;
import net.thumbtack.hospital.util.validator.annotation.Name;
import net.thumbtack.hospital.util.validator.annotation.Password;
import net.thumbtack.hospital.util.validator.annotation.Position;

import java.util.Objects;

public class AdminRegistrationDtoRequest {
    @Name
    private String firstName;
    @Name
    private String lastName;
    @Name(isPatronymic = true)
    private String patronymic;
    @Position
    private String position;
    @Login
    private String login;
    @Password
    private String password;

    public AdminRegistrationDtoRequest() {
    }

    public AdminRegistrationDtoRequest(String firstName, String lastName, String patronymic, String position,
                                       String login, String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setPosition(position);
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

    public void setPosition(String position) {
        this.position = position;
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

    public String getPosition() {
        return position;
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
        AdminRegistrationDtoRequest that = (AdminRegistrationDtoRequest) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(position, that.position) &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, position, login, password);
    }

    @Override
    public String toString() {
        return "AdminRegistrationDtoRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", position='" + position + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}