package net.thumbtack.hospital.dtoresponse.other.abstractresponse;

import java.util.Objects;

public abstract class UserInformationDtoResponse {
    private int id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String patronymic;

    public UserInformationDtoResponse() {
    }

    public UserInformationDtoResponse(int id, String login, String password, String firstName, String lastName, String patronymic) {
        setId(id);
        setLogin(login);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInformationDtoResponse response = (UserInformationDtoResponse) o;
        return id == response.id &&
                Objects.equals(login, response.login) &&
                Objects.equals(password, response.password) &&
                Objects.equals(firstName, response.firstName) &&
                Objects.equals(lastName, response.lastName) &&
                Objects.equals(patronymic, response.patronymic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, firstName, lastName, patronymic);
    }

    @Override
    public String toString() {
        return "UserInformationDtoResponse{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                '}';
    }
}