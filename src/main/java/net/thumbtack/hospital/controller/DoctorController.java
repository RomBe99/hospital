package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.service.DoctorService;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("DoctorController")
@RequestMapping("/api")
public class DoctorController {
    public static final String createMedicalCommissionUrl = "commissions";

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping(value = createMedicalCommissionUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CreateMedicalCommissionDtoResponse createMedicalCommission(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                                      @Valid @RequestBody CreateMedicalCommissionDtoRequest request) {
        return doctorService.createMedicalCommission(sessionId, request);
    }
}