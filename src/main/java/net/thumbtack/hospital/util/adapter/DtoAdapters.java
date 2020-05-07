package net.thumbtack.hospital.util.adapter;

import net.thumbtack.hospital.dtoresponse.other.schedulecell.ScheduleCellResponse;
import net.thumbtack.hospital.dtoresponse.other.schedulecell.ScheduleTimeCellResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.model.Patient;
import net.thumbtack.hospital.model.ScheduleCell;
import net.thumbtack.hospital.model.TimeCell;

import java.util.stream.Collectors;

public class DtoAdapters {
    public static ScheduleTimeCellResponse timeCellToScheduleTimeCellResponse(TimeCell tc) {
        return new ScheduleTimeCellResponse(tc.getTime().toString(),
                patientToPatientInformationDtoResponse(tc.getPatient()),
                tc.getDuration());
    }

    public static ScheduleCellResponse scheduleCellToScheduleCellResponse(ScheduleCell sc) {
        return new ScheduleCellResponse(sc.getDate().toString(),
                sc.getCells().stream()
                        .map(DtoAdapters::timeCellToScheduleTimeCellResponse)
                        .collect(Collectors.toList()));
    }

    public static PatientInformationDtoResponse patientToPatientInformationDtoResponse(Patient patient) {
        return new PatientInformationDtoResponse(patient.getId(),
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }
}