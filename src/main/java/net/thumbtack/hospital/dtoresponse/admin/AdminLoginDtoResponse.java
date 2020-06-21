package net.thumbtack.hospital.dtoresponse.admin;

import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;

import java.util.Objects;

public class AdminLoginDtoResponse extends LoginUserDtoResponse {
    private String position;

    public AdminLoginDtoResponse() {
    }

    public AdminLoginDtoResponse(int id, String firstName, String lastName, String patronymic, String position) {
        super(id, firstName, lastName, patronymic);

        setPosition(position);
    }

    public AdminLoginDtoResponse(int id, String firstName, String lastName, String position) {
        this(id, firstName, lastName, null, position);
    }

    public AdminLoginDtoResponse(String firstName, String lastName, String patronymic, String position) {
        this(0, firstName, lastName, patronymic, position);
    }

    public AdminLoginDtoResponse(String firstName, String lastName, String position) {
        this(firstName, lastName, null, position);
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AdminLoginDtoResponse that = (AdminLoginDtoResponse) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position);
    }

    @Override
    public String toString() {
        return super.toString() + " AdminLoginDtoResponse{" +
                "position='" + position + '\'' +
                '}';
    }
}