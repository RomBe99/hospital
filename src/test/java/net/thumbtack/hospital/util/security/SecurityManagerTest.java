package net.thumbtack.hospital.util.security;

import net.thumbtack.hospital.daoimpl.DaoTestApi;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SecurityManagerTest extends DaoTestApi {
    private final Administrator testAdministrator = new Administrator("TomsSobolev661", "ccACoYmctObr",
            "Томс", "Соболев", null, "Test admin");
    private final Patient testPatient = new Patient("GurKomarov702", "CypbSYxJpTMJ",
            "Гур", "Комаров", null, "lphhdymsficiooneqw@ttirv.com",
            "236970, г. Кодинск, ул. Восточная 2-я, дом 74, квартира 644", "89996685457");
    private final Doctor testDoctor = new Doctor("RyzhBetrozov526", "haaeaJbG1BbO",
            "Рыж", "Бетрозов", "Геннадьевич", "261", "Traumatologist", new ArrayList<>());

    @After
    public void resetUsersId() {
        testAdministrator.setId(0);
        testPatient.setId(0);
        testDoctor.setId(0);
    }

    @Test
    public void hasAdministratorPermissionsTest() throws PermissionDeniedException {
        insertUser(testAdministrator);

        String sessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        hasPermissions(sessionId, testAdministrator.getId(), UserType.ADMINISTRATOR);
    }

    @Test
    public void hasDoctorPermissionsTest() throws PermissionDeniedException {
        insertUser(testDoctor);

        String sessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        hasPermissions(sessionId, testDoctor.getId(), UserType.DOCTOR);
    }

    @Test
    public void hasPatientPermissionsTest() throws PermissionDeniedException {
        insertUser(testPatient);

        String sessionId = login(testPatient.getLogin(), testPatient.getPassword());
        hasPermissions(sessionId, testPatient.getId(), UserType.PATIENT);
    }

    @Test
    public void hasAdministratorAndPatientPermissionsTest() throws PermissionDeniedException {
        insertUser(testAdministrator);
        insertUser(testPatient);

        String adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        String patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        UserType[] permissions = {UserType.ADMINISTRATOR, UserType.PATIENT};

        hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
        hasPermissions(patientSessionId, testPatient.getId(), permissions);
    }

    @Test
    public void hasAdministratorAndDoctorPermissionsTest() throws PermissionDeniedException {
        insertUser(testAdministrator);
        insertUser(testDoctor);

        String adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        String doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        UserType[] permissions = {UserType.ADMINISTRATOR, UserType.DOCTOR};

        hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
        hasPermissions(doctorSessionId, testDoctor.getId(), permissions);
    }

    @Test
    public void hasPatientAndDoctorPermissionsTest() throws PermissionDeniedException {
        insertUser(testPatient);
        insertUser(testDoctor);

        String patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        String doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        UserType[] permissions = {UserType.PATIENT, UserType.DOCTOR};

        hasPermissions(patientSessionId, testPatient.getId(), permissions);
        hasPermissions(doctorSessionId, testDoctor.getId(), permissions);
    }

    @Test
    public void hasAdministratorAndDoctorAndPatientPermissionsTest() throws PermissionDeniedException {
        insertUser(testAdministrator);
        insertUser(testDoctor);
        insertUser(testPatient);

        String adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        String doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        String patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        UserType[] permissions = {UserType.ADMINISTRATOR, UserType.DOCTOR, UserType.PATIENT};

        hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
        hasPermissions(doctorSessionId, testDoctor.getId(), permissions);
        hasPermissions(patientSessionId, testPatient.getId(), permissions);
    }

    @Test
    public void administratorAndPatientHasNotDoctorPermissions() throws PermissionDeniedException {
        UserType permission = UserType.DOCTOR;

        insertUser(testAdministrator);
        insertUser(testPatient);
        insertUser(testDoctor);

        String adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        String patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        String doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());

        hasPermissions(doctorSessionId, testDoctor.getId(), permission);

        try {
            hasPermissions(adminSessionId, testAdministrator.getId(), permission);
            Assert.fail();
        } catch (PermissionDeniedException ignored) {
        }

        try {
            hasPermissions(patientSessionId, testPatient.getId(), permission);
            Assert.fail();
        } catch (PermissionDeniedException ignored) {
        }
    }

    @Test
    public void doctorAndPatientHasNotAdministratorPermissions() throws PermissionDeniedException {
        UserType permission = UserType.ADMINISTRATOR;

        insertUser(testAdministrator);
        insertUser(testPatient);
        insertUser(testDoctor);

        String adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        String patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        String doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());

        hasPermissions(adminSessionId, testAdministrator.getId(), permission);

        try {
            hasPermissions(doctorSessionId, testDoctor.getId(), permission);
            Assert.fail();
        } catch (PermissionDeniedException ignored) {
        }

        try {
            hasPermissions(patientSessionId, testPatient.getId(), permission);
            Assert.fail();
        } catch (PermissionDeniedException ignored) {
        }
    }

    @Test
    public void administratorAndDoctorHasNotPatientPermissions() throws PermissionDeniedException {
        UserType permission = UserType.PATIENT;

        insertUser(testAdministrator);
        insertUser(testPatient);
        insertUser(testDoctor);

        String adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
        String patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());
        String doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());

        hasPermissions(patientSessionId, testPatient.getId(), permission);

        try {
            hasPermissions(adminSessionId, testAdministrator.getId(), permission);
            Assert.fail();
        } catch (PermissionDeniedException ignored) {
        }

        try {
            hasPermissions(doctorSessionId, testDoctor.getId(), permission);
            Assert.fail();
        } catch (PermissionDeniedException ignored) {
        }
    }

    @Test(expected = PermissionDeniedException.class)
    public void doctorHasNotAdministratorAndPatientPermissionsTest() throws PermissionDeniedException {
        UserType[] permissions = {UserType.ADMINISTRATOR, UserType.PATIENT};
        String doctorSessionId = null;

        try {
            insertUser(testAdministrator);
            insertUser(testPatient);
            insertUser(testDoctor);

            String adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
            String patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());

            hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
            hasPermissions(patientSessionId, testPatient.getId(), permissions);

            doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        hasPermissions(doctorSessionId, testDoctor.getId(), permissions);
    }

    @Test(expected = PermissionDeniedException.class)
    public void patientHasNotAdministratorAndDoctorPermissionsTest() throws PermissionDeniedException {
        UserType[] permissions = {UserType.ADMINISTRATOR, UserType.DOCTOR};
        String patientSessionId = null;

        try {
            insertUser(testAdministrator);
            insertUser(testPatient);
            insertUser(testDoctor);

            String adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
            String doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
            patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());

            hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
            hasPermissions(doctorSessionId, testDoctor.getId(), permissions);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        hasPermissions(patientSessionId, testPatient.getId(), permissions);
    }

    @Test(expected = PermissionDeniedException.class)
    public void administratorHasNotPatientAndDoctorPermissionsTest() throws PermissionDeniedException {
        UserType[] permissions = {UserType.PATIENT, UserType.DOCTOR};
        String adminSessionId = null;

        try {
            insertUser(testAdministrator);
            insertUser(testPatient);
            insertUser(testDoctor);

            adminSessionId = login(testAdministrator.getLogin(), testAdministrator.getPassword());
            String doctorSessionId = login(testDoctor.getLogin(), testDoctor.getPassword());
            String patientSessionId = login(testPatient.getLogin(), testPatient.getPassword());

            hasPermissions(patientSessionId, testPatient.getId(), permissions);
            hasPermissions(doctorSessionId, testDoctor.getId(), permissions);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        hasPermissions(adminSessionId, testAdministrator.getId(), permissions);
    }
}