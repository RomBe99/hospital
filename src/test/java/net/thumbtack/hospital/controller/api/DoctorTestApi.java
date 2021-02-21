package net.thumbtack.hospital.controller.api;

import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;

public interface DoctorTestApi {
    CreateMedicalCommissionDtoResponse createMedicalCommission(String sessionId, CreateMedicalCommissionDtoRequest request) throws Exception;
}