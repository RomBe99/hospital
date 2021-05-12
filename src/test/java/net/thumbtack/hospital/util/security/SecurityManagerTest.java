package net.thumbtack.hospital.util.security;

import net.thumbtack.hospital.daoimpl.DaoTestApi;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SecurityManagerTest extends DaoTestApi {
    private final Administrator testAdministrator = new Administrator("TomsSobolev661", "ccACoYmctObr",
            "Томс", "Соболев", null, "Test admin");
    private final Patient testPatient = new Patient("GurKomarov702", "CypbSYxJpTMJ",
            "Гур", "Комаров", null, "lphhdymsficiooneqw@ttirv.com",
            "236970, г. Кодинск, ул. Восточная 2-я, дом 74, квартира 644", "89996685457");
    private final Doctor testDoctor = new Doctor("RyzhBetrozov526", "haaeaJbG1BbO",
            "Рыж", "Бетрозов", "Геннадьевич", "261", "Traumatologist", new ArrayList<>());

    @AfterEach
    public void resetUsersId() {
        testAdministrator.setId(0);
        testPatient.setId(0);
        testDoctor.setId(0);
    }

    @Test
    public void hasAdministratorPermissionsTest() throws PermissionDeniedException {
        insertUser(testAdministrator);

        final var sessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        hasPermissions(sessionId, testAdministrator.getId(), UserType.ADMINISTRATOR);
    }

    @Test
    public void hasDoctorPermissionsTest() throws PermissionDeniedException {
        insertUser(testDoctor);

        final var sessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        hasPermissions(sessionId, testDoctor.getId(), UserType.DOCTOR);
    }

    @Test
    public void hasPatientPermissionsTest() throws PermissionDeniedException {
        insertUser(testPatient);

        final var sessionId = login(testPatient.getLogin(), testPatient.getPassword());
        hasPermissions(sessionId, testPatient.getId(), UserType.PATIENT);
    }

    @Test
    public void hasAdministratorAndPatientPermissionsTest() throws PermissionDeniedException {
        insertUser(testAdministrator);
        insertUser(testPatient);

        final var adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        final var patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        final var permissions = new UserType[]{UserType.ADMINISTRATOR, UserType.PATIENT};

        hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
        hasPermissions(patientSessionId, testPatient.getId(), permissions);
    }

    @Test
    public void hasAdministratorAndDoctorPermissionsTest() throws PermissionDeniedException {
        insertUser(testAdministrator);
        insertUser(testDoctor);

        final var adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        final var doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        final var permissions = new UserType[]{UserType.ADMINISTRATOR, UserType.DOCTOR};

        hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
        hasPermissions(doctorSessionId, testDoctor.getId(), permissions);
    }

    @Test
    public void hasPatientAndDoctorPermissionsTest() throws PermissionDeniedException {
        insertUser(testPatient);
        insertUser(testDoctor);

        final var patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        final var doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        final var permissions = new UserType[]{UserType.PATIENT, UserType.DOCTOR};

        hasPermissions(patientSessionId, testPatient.getId(), permissions);
        hasPermissions(doctorSessionId, testDoctor.getId(), permissions);
    }

    @Test
    public void hasAdministratorAndDoctorAndPatientPermissionsTest() throws PermissionDeniedException {
        insertUser(testAdministrator);
        insertUser(testDoctor);
        insertUser(testPatient);

        final var adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        final var doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        final var patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        final var permissions = new UserType[]{UserType.ADMINISTRATOR, UserType.DOCTOR, UserType.PATIENT};

        hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
        hasPermissions(doctorSessionId, testDoctor.getId(), permissions);
        hasPermissions(patientSessionId, testPatient.getId(), permissions);
    }

    @Test
    public void administratorAndPatientHasNotDoctorPermissions() throws PermissionDeniedException {
        final var permission = UserType.DOCTOR;

        insertUser(testAdministrator);
        insertUser(testPatient);
        insertUser(testDoctor);

        final var adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        final var patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        final var doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());

        hasPermissions(doctorSessionId, testDoctor.getId(), permission);

        try {
            hasPermissions(adminSessionId, testAdministrator.getId(), permission);
            Assertions.fail();
        } catch (PermissionDeniedException ignored) {
        }

        try {
            hasPermissions(patientSessionId, testPatient.getId(), permission);
            Assertions.fail();
        } catch (PermissionDeniedException ignored) {
        }
    }

    @Test
    public void doctorAndPatientHasNotAdministratorPermissions() throws PermissionDeniedException {
        final var permission = UserType.ADMINISTRATOR;

        insertUser(testAdministrator);
        insertUser(testPatient);
        insertUser(testDoctor);

        final var adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        final var patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        final var doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());

        hasPermissions(adminSessionId, testAdministrator.getId(), permission);

        try {
            hasPermissions(doctorSessionId, testDoctor.getId(), permission);
            Assertions.fail();
        } catch (PermissionDeniedException ignored) {
        }

        try {
            hasPermissions(patientSessionId, testPatient.getId(), permission);
            Assertions.fail();
        } catch (PermissionDeniedException ignored) {
        }
    }

    @Test
    public void administratorAndDoctorHasNotPatientPermissions() throws PermissionDeniedException {
        final var permission = UserType.PATIENT;

        insertUser(testAdministrator);
        insertUser(testPatient);
        insertUser(testDoctor);

        final var adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        final var patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        final var doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());

        hasPermissions(patientSessionId, testPatient.getId(), permission);

        try {
            hasPermissions(adminSessionId, testAdministrator.getId(), permission);
            Assertions.fail();
        } catch (PermissionDeniedException ignored) {
        }

        try {
            hasPermissions(doctorSessionId, testDoctor.getId(), permission);
            Assertions.fail();
        } catch (PermissionDeniedException ignored) {
        }
    }

    @Test
    public void doctorHasNotAdministratorAndPatientPermissionsTest() throws PermissionDeniedException {
        final var permissions = new UserType[]{UserType.ADMINISTRATOR, UserType.PATIENT};

        insertUser(testAdministrator);
        insertUser(testPatient);
        insertUser(testDoctor);

        final var adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        final var patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());

        hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
        hasPermissions(patientSessionId, testPatient.getId(), permissions);

        final var doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());

        Assertions.assertThrows(PermissionDeniedException.class, () -> hasPermissions(doctorSessionId, testDoctor.getId(), permissions));
    }

    @Test
    public void patientHasNotAdministratorAndDoctorPermissionsTest() throws PermissionDeniedException {
        final var permissions = new UserType[]{UserType.ADMINISTRATOR, UserType.DOCTOR};

        insertUser(testAdministrator);
        insertUser(testPatient);
        insertUser(testDoctor);

        final var adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        final var doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        final var patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());

        hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
        hasPermissions(doctorSessionId, testDoctor.getId(), permissions);

        Assertions.assertThrows(PermissionDeniedException.class, () -> hasPermissions(patientSessionId, testPatient.getId(), permissions));
    }

    @Test
    public void administratorHasNotPatientAndDoctorPermissionsTest() throws PermissionDeniedException {
        final var permissions = new UserType[]{UserType.PATIENT, UserType.DOCTOR};

        insertUser(testAdministrator);
        insertUser(testPatient);
        insertUser(testDoctor);

        final var adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        final var doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        final var patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());

        hasPermissions(patientSessionId, testPatient.getId(), permissions);
        hasPermissions(doctorSessionId, testDoctor.getId(), permissions);

        Assertions.assertThrows(PermissionDeniedException.class, () -> hasPermissions(adminSessionId, testAdministrator.getId(), permissions));
    }
}