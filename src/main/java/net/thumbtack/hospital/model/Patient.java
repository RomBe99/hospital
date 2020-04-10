package net.thumbtack.hospital.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO Добавить валидацию через аннотации
public class Patient extends User {
    private List<ScheduleCell> tickets;

    public Patient(int id,
                   String login, String password,
                   String firstName, String lastName, String patronymic,
                   List<ScheduleCell> tickets) {
        super(id, login, password, firstName, lastName, patronymic);
        this.tickets = tickets;
    }

    public Patient(int id,
                   String login, String password,
                   String firstName, String lastName, String patronymic) {
        this(id, login, password, firstName, lastName, patronymic, new ArrayList<>());
    }

    public Patient(String login, String password,
                   String firstName, String lastName, String patronymic) {
        this(0, login, password, firstName, lastName, patronymic);
    }

    public void setTickets(List<ScheduleCell> tickets) {
        this.tickets = tickets;
    }

    public List<ScheduleCell> getTickets() {
        return tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(getTickets(), patient.getTickets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTickets());
    }

    @Override
    public String toString() {
        return super.toString() + "\nPatient{" +
                "tickets=" + tickets +
                '}';
    }
}