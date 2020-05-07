package net.thumbtack.hospital.model;

import java.util.Objects;

public class Administrator extends User {
    private String position;

    public Administrator() {
    }

    public Administrator(int id,
                         String login, String password,
                         String firstName, String lastName, String patronymic,
                         String position) {
        super(id, login, password, firstName, lastName, patronymic);

        setPosition(position);
    }

    public Administrator(String login, String password,
                         String firstName, String lastName, String patronymic,
                         String position) {
        this(0, login, password, firstName, lastName, patronymic, position);
    }

    public Administrator(int id, String password,
                         String firstName, String lastName, String patronymic,
                         String position) {
        this(id,"", password, firstName, lastName, patronymic, position);
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
        if (!(o instanceof Administrator)) return false;
        if (!super.equals(o)) return false;
        Administrator that = (Administrator) o;
        return Objects.equals(getPosition(), that.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPosition());
    }

    @Override
    public String toString() {
        return super.toString() + " Administrator{" +
                "position='" + position + '\'' +
                '}';
    }
}