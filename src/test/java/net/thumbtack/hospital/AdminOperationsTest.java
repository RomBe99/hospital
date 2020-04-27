package net.thumbtack.hospital;

import net.thumbtack.hospital.model.Administrator;
import org.junit.Assert;
import org.junit.Test;

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

    // TODO Добавить тесты нарушающие правила валидации
}