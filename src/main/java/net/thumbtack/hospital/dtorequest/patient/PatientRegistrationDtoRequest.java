package net.thumbtack.hospital.dtorequest.patient;

import net.thumbtack.hospital.util.validator.annotation.*;

import javax.validation.constraints.Email;
import java.util.Objects;

public class PatientRegistrationDtoRequest {
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
    @Login
    private String login;
    @Password
    private String password;

    public PatientRegistrationDtoRequest() {
    }

    public PatientRegistrationDtoRequest(String firstName, String lastName, String patronymic,
                                         String email, String address, String phone,
                                         String login, String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setEmail(email);
        setAddress(address);
        setPhone(phone);
        setLogin(login);
        setPassword(password);
    }

    public PatientRegistrationDtoRequest(String firstName, String lastName,
                                         String email, String address, String phone,
                                         String login, String password) {
        this(firstName, lastName, null, email, address, phone, login, password);
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

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
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
        PatientRegistrationDtoRequest that = (PatientRegistrationDtoRequest) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(email, that.email) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, email, address, phone, login, password);
    }

    @Override
    public String toString() {
        return "PatientRegistrationDtoRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}