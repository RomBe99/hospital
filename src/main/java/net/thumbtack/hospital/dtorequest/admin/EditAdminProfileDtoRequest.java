package net.thumbtack.hospital.dtorequest.admin;

import net.thumbtack.hospital.util.validator.annotation.Name;
import net.thumbtack.hospital.util.validator.annotation.Password;
import net.thumbtack.hospital.util.validator.annotation.Position;

import java.util.Objects;

public class EditAdminProfileDtoRequest {
    @Name
    private String firstName;
    @Name
    private String lastName;
    @Name(isPatronymic = true)
    private String patronymic;
    @Position
    private String position;
    @Password
    private String oldPassword;
    @Password
    private String newPassword;

    public EditAdminProfileDtoRequest() {
    }

    public EditAdminProfileDtoRequest(String firstName, String lastName, String patronymic,
                                      String position,
                                      String oldPassword, String newPassword) {
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setPosition(position);
        setOldPassword(oldPassword);
        setNewPassword(newPassword);
    }

    public EditAdminProfileDtoRequest(String firstName, String lastName, String position,
                                      String oldPassword, String newPassword) {
        this(firstName, lastName, null, position, oldPassword, newPassword);
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

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditAdminProfileDtoRequest that = (EditAdminProfileDtoRequest) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(position, that.position) &&
                Objects.equals(oldPassword, that.oldPassword) &&
                Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, position, oldPassword, newPassword);
    }

    @Override
    public String toString() {
        return "EditAdminProfileDtoRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", position='" + position + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}