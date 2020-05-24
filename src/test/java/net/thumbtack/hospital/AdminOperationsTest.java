package net.thumbtack.hospital;

import net.thumbtack.hospital.model.Administrator;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.Patient;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class AdminOperationsTest extends BaseTest {
    @Test
    public void insertAdministratorTest() {
        String login = "VorobevaAltina312";
        String password = "fwZO9tIyAQlf";
        String firstName = "Альтина";
        String lastName = "Воробьёва";
        String patronymic = "Максимовна";
        String position = "Test Admin";

        Administrator expectedAdmin = new Administrator(login, password, firstName, lastName, patronymic, position);

        try {
            Administrator insertedAdmin = insertAdministrator(login, password, firstName, lastName, patronymic, position);
            expectedAdmin.setId(insertedAdmin.getId());

            Assert.assertEquals(expectedAdmin, insertedAdmin);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void updateAdministratorTest1() {
        String login = "VorobevaAltina312";
        String password = "fwZO9tIyAQlf";
        String firstName = "Альтина";
        String lastName = "Воробьёва";
        String patronymic = "Максимовна";
        String position = "Test Admin";

        Administrator expectedAdmin = new Administrator(login, password, firstName, lastName, patronymic, position);

        try {
            Administrator insertedAdmin = insertAdministrator(login, password, firstName, lastName, patronymic, position);
            expectedAdmin.setId(insertedAdmin.getId());

            Assert.assertEquals(expectedAdmin, insertedAdmin);

            expectedAdmin.setPosition("Test update position");

            adminDao.updateAdministrator(expectedAdmin);

            Administrator updatedAdmin = adminDao.getAdministratorById(expectedAdmin.getId());
            Assert.assertEquals(expectedAdmin, updatedAdmin);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void updateAdministratorTest2() {
        String login = "VorobevaAltina312";
        String password = "fwZO9tIyAQlf";
        String firstName = "Альтина";
        String lastName = "Воробьёва";
        String patronymic = "Максимовна";
        String position = "Test Admin";

        Administrator expectedAdmin = new Administrator(login, password, firstName, lastName, patronymic, position);

        try {
            Administrator insertedAdmin = insertAdministrator(login, password, firstName, lastName, patronymic, position);
            expectedAdmin.setId(insertedAdmin.getId());

            Assert.assertEquals(expectedAdmin, insertedAdmin);

            expectedAdmin.setPosition("Test update position");
            expectedAdmin.setPatronymic("Геннадиевна");

            adminDao.updateAdministrator(expectedAdmin);

            Administrator updatedAdmin = adminDao.getAdministratorById(expectedAdmin.getId());
            Assert.assertEquals(expectedAdmin, updatedAdmin);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void removeAdministratorTest() {
        String login = "VorobevaAltina312";
        String password = "fwZO9tIyAQlf";
        String firstName = "Альтина";
        String lastName = "Воробьёва";
        String patronymic = "Максимовна";
        String position = "Test Admin";

        Administrator expectedAdmin = new Administrator(login, password, firstName, lastName, patronymic, position);

        try {
            Administrator insertedAdmin = insertAdministrator(login, password, firstName, lastName, patronymic, position);
            expectedAdmin.setId(insertedAdmin.getId());

            Assert.assertEquals(expectedAdmin, insertedAdmin);

            adminDao.removeAdministratorById(expectedAdmin.getId());

            Administrator removedAdmin = adminDao.getAdministratorById(expectedAdmin.getId());
            Assert.assertNull(removedAdmin);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void administratorPermissionsTest() {
        String login = "VorobevaAltina312";
        String password = "fwZO9tIyAQlf";
        String firstName = "Альтина";
        String lastName = "Воробьёва";
        String patronymic = "Максимовна";
        String position = "Test Admin";

        Administrator expectedAdmin = new Administrator(login, password, firstName, lastName, patronymic, position);

        try {
            Administrator insertedAdmin = insertAdministrator(login, password, firstName, lastName, patronymic, position);
            expectedAdmin.setId(insertedAdmin.getId());

            Assert.assertEquals(expectedAdmin, insertedAdmin);

            String sessionId = UUID.randomUUID().toString();
            int adminIdWhenLogin = adminDao.login(sessionId, expectedAdmin.getLogin(), expectedAdmin.getPassword());

            Assert.assertEquals(insertedAdmin.getId(), adminIdWhenLogin);

            int idAfterCheckingPermissions = adminDao.hasPermissions(sessionId);

            Assert.assertEquals(adminIdWhenLogin, idAfterCheckingPermissions);
        } catch (RuntimeException | PermissionDeniedException ex) {
            Assert.fail();
        }
    }

    @Test(expected = PermissionDeniedException.class)
    public void tryGetAdministratorPermissionsForDoctor() throws PermissionDeniedException {
        String login = "VorobevaAltina312";
        String password = "fwZO9tIyAQlf";
        String firstName = "Альтина";
        String lastName = "Воробьёва";
        String patronymic = "Максимовна";
        String cabinet = "306";
        String speciality = "Surgeon";

        Doctor expectedDoctor = new Doctor(login, password, firstName, lastName, patronymic, cabinet, speciality);

        try {
            Doctor insertedDoctor = insertDoctor(login, password, firstName, lastName, patronymic, cabinet, speciality);
            expectedDoctor.setId(insertedDoctor.getId());

            Assert.assertEquals(expectedDoctor, insertedDoctor);

            String sessionId = UUID.randomUUID().toString();
            int doctorIdWhenLogin = doctorDao.login(sessionId, login, password);

            Assert.assertEquals(insertedDoctor.getId(), doctorIdWhenLogin);

            adminDao.hasPermissions(sessionId);
        } catch (RuntimeException e) {
            Assert.fail();
        }
    }

    @Test(expected = PermissionDeniedException.class)
    public void tryGetAdministratorPermissionsForPatient() throws PermissionDeniedException {
        String login = "VorobevaAltina312";
        String password = "fwZO9tIyAQlf";
        String firstName = "Альтина";
        String lastName = "Воробьёва";
        String patronymic = "Максимовна";
        String email = "xikox93966@ximtyl.com";
        String phone = "+79585825122";
        String address = "152042, г. Новоегорьевское, ул. Волжский б-р, дом 95, квартира 181";

        Patient expectedPatient = new Patient(login, password, firstName, lastName, patronymic, email, address, phone);

        try {
            Patient insertedPatient = insertPatient(login, password, firstName, lastName, patronymic, email, address, phone);
            expectedPatient.setId(insertedPatient.getId());

            Assert.assertEquals(expectedPatient, insertedPatient);

            String sessionId = UUID.randomUUID().toString();
            int patientIdWhenLogin = doctorDao.login(sessionId, login, password);

            Assert.assertEquals(insertedPatient.getId(), patientIdWhenLogin);

            adminDao.hasPermissions(sessionId);
        } catch (RuntimeException e) {
            Assert.fail();
        }
    }
}