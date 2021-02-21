package net.thumbtack.hospital.controller.api;

import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtoresponse.admin.AdminRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditAdminProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditDoctorScheduleDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;

public interface AdministratorTestApi {
    AdminRegistrationDtoResponse administratorRegistration(String sessionId, AdminRegistrationDtoRequest request) throws Exception;

    DoctorRegistrationDtoResponse doctorRegistration(String sessionId, DoctorRegistrationDtoRequest request) throws Exception;

    EditAdminProfileDtoResponse editAdministratorProfile(String sessionId, EditAdminProfileDtoRequest request) throws Exception;

    EditDoctorScheduleDtoResponse editDoctorSchedule(String sessionId, int doctorId, EditDoctorScheduleDtoRequest request) throws Exception;

    EmptyDtoResponse removeDoctor(String sessionId, int doctorId, RemoveDoctorDtoRequest request) throws Exception;
}