package net.thumbtack.hospital.dtoresponse.admin;

import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;

import java.util.Objects;

public class AdminInformationDtoResponse extends UserInformationDtoResponse {
    private String position;

    public AdminInformationDtoResponse() {
    }

    public AdminInformationDtoResponse(int id,
                                       String login, String password,
                                       String firstName, String lastName, String patronymic,
                                       String position) {
        super(id, login, password, firstName, lastName, patronymic);

        setPosition(position);
    }

    public AdminInformationDtoResponse(int id,
                                       String login, String password,
                                       String firstName, String lastName,
                                       String position) {
        this(id, login, password, firstName, lastName, null, position);
    }

    public AdminInformationDtoResponse(String login, String password,
                                       String firstName, String lastName, String patronymic,
                                       String position) {
        this(0, login, password, firstName, lastName, patronymic, position);
    }

    public AdminInformationDtoResponse(String login, String password,
                                       String firstName, String lastName,
                                       String position) {
        this(0, login, password, firstName, lastName, null, position);
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
        AdminInformationDtoResponse that = (AdminInformationDtoResponse) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position);
    }

    @Override
    public String toString() {
        return super.toString() + " AdminInformationDtoResponse{" +
                "position='" + position + '\'' +
                '}';
    }
}