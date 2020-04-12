package net.thumbtack.hospital.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO Добавить валидацию через аннотации
public class Patient extends User {
    // FIXME Добавить недостающие поля
    private String email;
    private String address;
    private String phone;
    private List<ScheduleCell> tickets;

    public Patient(int id,
                   String login, String password,
                   String firstName, String lastName, String patronymic,
                   String email, String address, String phone, List<ScheduleCell> tickets) {
        super(id, login, password, firstName, lastName, patronymic);

        setEmail(email);
        setAddress(address);
        setPhone(phone);
        setTickets(tickets);
    }

    public Patient(int id,
                   String login, String password,
                   String firstName, String lastName, String patronymic,
                   String email, String address, String phone) {
        this(id, login, password, firstName, lastName, patronymic, email, address, phone, new ArrayList<>());
    }

    public Patient(String login, String password,
                   String firstName, String lastName, String patronymic,
                   String email, String address, String phone) {
        this(0, login, password, firstName, lastName, patronymic, email, address, phone);
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

    public void setTickets(List<ScheduleCell> tickets) {
        this.tickets = tickets;
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

    public List<ScheduleCell> getTickets() {
        return tickets;
    }
}