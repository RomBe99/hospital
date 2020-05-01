package net.thumbtack.hospital.dtorequest.admin;

import java.util.Objects;

public class RemoveDoctorDtoRequest {
    private String date;

    public RemoveDoctorDtoRequest() {
    }

    public RemoveDoctorDtoRequest(String date) {
        this.date = date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveDoctorDtoRequest that = (RemoveDoctorDtoRequest) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public String toString() {
        return "RemoveDoctorDtoRequest{" +
                "date='" + date + '\'' +
                '}';
    }
}