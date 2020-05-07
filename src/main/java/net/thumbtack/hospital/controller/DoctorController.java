package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DoctorController {
    private static final String createMedicalCommissionUrl = "/api/commissions";

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping(value = createMedicalCommissionUrl)
    public RequestEntity<String> createMedicalCommission(@CookieValue(value = "session.id") int sessionId,
                                                         @Valid @RequestBody CreateMedicalCommissionDtoRequest request) {
        return null;
    }
}