package net.thumbtack.hospital.dtoresponse.user;

import net.thumbtack.hospital.dtoresponse.doctor.DoctorInformationDtoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetAllDoctorsDtoResponse {
    private List<DoctorInformationDtoResponse> doctors = new ArrayList<>();

    public GetAllDoctorsDtoResponse() {
    }

    public GetAllDoctorsDtoResponse(List<DoctorInformationDtoResponse> doctors) {
        setDoctors(doctors);
    }

    public void setDoctors(List<DoctorInformationDtoResponse> doctors) {
        this.doctors = doctors;
    }

    public List<DoctorInformationDtoResponse> getDoctors() {
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