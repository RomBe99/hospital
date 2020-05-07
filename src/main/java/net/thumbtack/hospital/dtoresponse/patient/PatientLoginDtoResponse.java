package net.thumbtack.hospital.dtoresponse.patient;

import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;

import java.util.Objects;

public class PatientLoginDtoResponse extends LoginUserDtoResponse {
    private String email;
    private String address;
    private String phone;

    public PatientLoginDtoResponse() {
    }

    public PatientLoginDtoResponse(int id, String firstName, String lastName, String patronymic, String email, String address, String phone) {
        super(id, firstName, lastName, patronymic);

        setEmail(email);
        setAddress(address);
        setPhone(phone);
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
        if (!super.equals(o)) return false;
        PatientLoginDtoResponse that = (PatientLoginDtoResponse) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, address, phone);
    }

    @Override
    public String toString() {
        return super.toString() + " PatientLoginDtoResponse{" +
                "email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}