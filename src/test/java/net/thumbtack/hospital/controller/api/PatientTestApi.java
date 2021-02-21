package net.thumbtack.hospital.controller.api;

import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.AppointmentToDoctorDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.EditPatientProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.ticket.AllTicketsDtoResponse;
import org.apache.commons.lang3.tuple.Pair;

public interface PatientTestApi {
    Pair<String, PatientRegistrationDtoResponse> patientRegistration(PatientRegistrationDtoRequest request) throws Exception;

    EditPatientProfileDtoResponse editPatientProfile(String sessionId, EditPatientProfileDtoRequest request) throws Exception;

    AppointmentToDoctorDtoResponse appointmentToDoctor(String sessionId, AppointmentToDoctorDtoRequest request) throws Exception;

    EmptyDtoResponse denyTicketToDoctor(String sessionId, String ticketTitle) throws Exception;

    AllTicketsDtoResponse getTickets(String sessionId) throws Exception;
}