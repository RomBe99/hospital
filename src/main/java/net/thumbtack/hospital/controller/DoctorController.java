package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class DoctorController {
    private static final String createMedicalCommissionUrl = "commissions";

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping(value = createMedicalCommissionUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createMedicalCommission(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                                          @Valid @RequestBody CreateMedicalCommissionDtoRequest request) {
        return null;
    }
}