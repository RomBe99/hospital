package net.thumbtack.hospital.dtoresponse.patient;

import java.util.Objects;

public class PatientInformationDtoResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String address;
    private String phone;

    public PatientInformationDtoResponse() {
    }

    public PatientInformationDtoResponse(int id, String firstName, String lastName, String patronymic,
                                         String email, String address, String phone) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setEmail(email);
        setAddress(address);
        setPhone(phone);
    }

    public PatientInformationDtoResponse(int id, String firstName, String lastName,
                                         String email, String address, String phone) {
        this(id, firstName, lastName, null, email, address, phone);
    }

    public PatientInformationDtoResponse(String firstName, String lastName, String patronymic,
                                         String email, String address, String phone) {
        this(0, firstName, lastName, patronymic, email, address, phone);
    }

    public PatientInformationDtoResponse(String firstName, String lastName,
                                         String email, String address, String phone) {
        this(0, firstName, lastName, null, email, address, phone);
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientInformationDtoResponse that = (PatientInformationDtoResponse) o;
        return id == that.id &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(email, that.email) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, email, address, phone);
    }

    @Override
    public String toString() {
        return "PatientInformationDtoResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}