package net.thumbtack.hospital.dtoresponse.user;

import java.util.List;
import java.util.Objects;

public class GetAllDoctorsDtoResponse {
    private List<GetDoctorDtoResponse> doctors;

    public GetAllDoctorsDtoResponse() {
    }

    public GetAllDoctorsDtoResponse(List<GetDoctorDtoResponse> doctors) {
        setDoctors(doctors);
    }

    public void setDoctors(List<GetDoctorDtoResponse> doctors) {
        this.doctors = doctors;
    }

    public List<GetDoctorDtoResponse> getDoctors() {
        return doctors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetAllDoctorsDtoResponse that = (GetAllDoctorsDtoResponse) o;
        return Objects.equals(doctors, that.doctors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctors);
    }

    @Override
    public String toString() {
        return "GetAllDoctorsDtoResponse{" +
                "doctors=" + doctors +
                '}';
    }
}