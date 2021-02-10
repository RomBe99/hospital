package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import org.junit.Test;

import java.util.Collections;

public class CommonDaoTest extends DaoTestApi {
    @Test
    public void getAdministratorTypeByUserId() {
        Administrator administrator = new Administrator("KylieMccalpin345", "qXRf0ujqF501",
                "Kylie", "Mccalpin", null, "Test admin");
        insertUser(administrator);

        getUserTypeByUserId(administrator.getId(), UserType.ADMINISTRATOR.getType());
    }

    @Test
    public void getPatientTypeByUserId() {
        Patient patient = new Patient("CasenTurns398", "yC6JZ7YHqd6F",
                "Casen", "Turns", null, "jgmfqxbdjvzgikyklu@awdrt.net",
                "461541, г. Выборг, ул. Пименовский туп, дом 159, квартира 414", "+79530654217");
        insertUser(patient);

        getUserTypeByUserId(patient.getId(), UserType.PATIENT.getType());
    }

    @Test
    public void getDoctorTypeByUserId() {
        Doctor doctor = new Doctor("RoudiKarpov62", "EaGV1fBkLxrX",
                "Роуди", "Карпов", "Артемович", "306", "Therapist", Collections.emptyList());
        insertUser(doctor);

        getUserTypeByUserId(doctor.getId(), UserType.DOCTOR.getType());
    }
}