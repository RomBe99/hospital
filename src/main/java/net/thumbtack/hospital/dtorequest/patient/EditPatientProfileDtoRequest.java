package net.thumbtack.hospital.dtorequest.patient;

import net.thumbtack.hospital.util.validator.annotation.Address;
import net.thumbtack.hospital.util.validator.annotation.Name;
import net.thumbtack.hospital.util.validator.annotation.Password;
import net.thumbtack.hospital.util.validator.annotation.Phone;

import javax.validation.constraints.Email;
import java.util.Objects;

public class EditPatientProfileDtoRequest {
    @Name
    private String firstName;
    @Name
    private String lastName;
    @Name(isPatronymic = true)
    private String patronymic;
    @Email
    private String email;
    @Address
    private String address;
    @Phone
    private String phone;
    @Password
    private String oldPassword;
    @Password
    private String newPassword;

    public EditPatientProfileDtoRequest() {
    }

    public EditPatientProfileDtoRequest(String firstName, String lastName, String patronymic,
                                        String email, String address, String phone,
                                        String oldPassword, String newPassword) {
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setEmail(email);
        setAddress(address);
        setPhone(phone);
        setOldPassword(oldPassword);
        setNewPassword(newPassword);
    }

    public EditPatientProfileDtoRequest(String firstName, String lastName,
                                        String email, String address, String phone,
                                        String oldPassword, String newPassword) {
        this(firstName, lastName, null, email, address, phone, oldPassword, newPassword);
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
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
        EditPatientProfileDtoRequest that = (EditPatientProfileDtoRequest) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(email, that.email) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(oldPassword, that.oldPassword) &&
                Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, email, address, phone, oldPassword, newPassword);
    }

    @Override
    public String toString() {
        return "EditPatientProfileDtoRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}