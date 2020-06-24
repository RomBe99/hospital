package net.thumbtack.hospital.dtoresponse.admin;

import java.util.Objects;

public class AdminRegistrationDtoResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;

    public AdminRegistrationDtoResponse() {
    }

    public AdminRegistrationDtoResponse(int id, String firstName, String lastName, String patronymic, String position) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setPosition(position);
    }

    public AdminRegistrationDtoResponse(int id, String firstName, String lastName, String position) {
        this(id, firstName, lastName, null, position);
    }

    public AdminRegistrationDtoResponse(String firstName, String lastName, String patronymic, String position) {
        this(0, firstName, lastName, patronymic, position);
    }

    public AdminRegistrationDtoResponse(String firstName, String lastName, String position) {
        this(firstName, lastName, null, position);
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

    public void setPosition(String position) {
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminRegistrationDtoResponse response = (AdminRegistrationDtoResponse) o;
        return id == response.id &&
                Objects.equals(firstName, response.firstName) &&
                Objects.equals(lastName, response.lastName) &&
                Objects.equals(patronymic, response.patronymic) &&
                Objects.equals(position, response.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, position);
    }

    @Override
    public String toString() {
        return "AdminRegistrationDtoResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}