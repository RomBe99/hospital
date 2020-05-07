package net.thumbtack.hospital.dtoresponse.other.abstractresponse;

import java.util.Objects;

public abstract class LoginUserDtoResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;

    public LoginUserDtoResponse() {
    }

    public LoginUserDtoResponse(int id, String firstName, String lastName, String patronymic) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginUserDtoResponse response = (LoginUserDtoResponse) o;
        return id == response.id &&
                Objects.equals(firstName, response.firstName) &&
                Objects.equals(lastName, response.lastName) &&
                Objects.equals(patronymic, response.patronymic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic);
    }

    @Override
    public String toString() {
        return "LoginUserDtoResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                '}';
    }
}