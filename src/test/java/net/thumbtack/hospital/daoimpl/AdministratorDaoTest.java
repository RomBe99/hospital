package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.user.Administrator;
import org.junit.Assert;
import org.junit.Test;

public class AdministratorDaoTest extends DaoTestApi {
    @Test
    public void loginRootAdminTest() {
        String sessionId = loginRootAdmin();
        logout(sessionId);
    }

    @Test
    public void insertAdministratorWithLoginAndLogoutTest() {
        Administrator administrator = new Administrator("VorobevaAltina312", "fwZO9tIyAQlf",
                "Альтина", "Воробьёва", "Максимовна", "Test Admin");
        insertUser(administrator);

        String sessionId = login(administrator.getLogin(), administrator.getPassword());
        logout(sessionId);
    }

    @Test
    public void updateAdministratorTest1() {
        Administrator administrator = new Administrator("IoanniShershova328", "rKEKaMzcqI9K",
                "Иоанни", "Шершова", "Петровна", "Test Admin");
        insertUser(administrator);

        String sessionId = login(administrator.getLogin(), administrator.getPassword());

        administrator.setPosition("Very Important Person");
        updateUser(administrator, administrator.getPassword());

        logout(sessionId);
    }

    @Test
    public void updateAdministratorTest2() {
        Administrator administrator = new Administrator("HoroborIlin727", "QlK14GlXJGf7",
                "Хоробор", "Ильин", "Закирович", "Test Admin");
        insertUser(administrator);

        String sessionId = login(administrator.getLogin(), administrator.getPassword());
        String newPassword = "NFLPcdfQy5JM";

        administrator.setFirstName("Кулун");
        administrator.setLastName("Ковалевский");
        administrator.setPatronymic("Артемович");
        administrator.setPosition("Another admin");
        updateUser(administrator, newPassword);

        logout(sessionId);
    }

    @Test
    public void updateAdministratorTest3() {
        Administrator administrator = new Administrator("LayzaLermontova350", "P6IPEJLd5ktp",
                "Лайза", "Лермонтова", "Тарасовна", "Test Admin");
        insertUser(administrator);

        String sessionId = login(administrator.getLogin(), administrator.getPassword());
        String newPassword = "NFLPcdfQy5JM";

        administrator.setPatronymic("");
        updateUser(administrator, newPassword);

        administrator.setPatronymic(null);
        updateUser(administrator, newPassword);

        logout(sessionId);
    }

    @Test
    public void removeAdministratorTest() {
        Administrator administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Тестовый администратор");
        insertUser(administrator);

        removeUserById(administrator.getId(), administrator.getClass());
    }

    @Test(expected = RuntimeException.class)
    public void insertAdministratorsWithSameLoginTest() {
        Administrator administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");

        try {
            insertUser(administrator);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        insertUser(administrator);
    }

    @Test(expected = RuntimeException.class)
    public void insertAdministratorWithIncorrectLoginTest() {
        Administrator administrator = new Administrator(null, "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");
        insertUser(administrator);
    }

    @Test(expected = RuntimeException.class)
    public void insertAdministratorWithIncorrectPasswordTest() {
        Administrator administrator = new Administrator("HendonGurov725", null,
                "Хендон", "Гуров", null, "Admin with incorrect data");
        insertUser(administrator);
    }

    @Test(expected = RuntimeException.class)
    public void updateAdministratorWithIncorrectPasswordTest() {
        Administrator administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");

        try {
            insertUser(administrator);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        updateUser(administrator, null);
    }

    @Test(expected = RuntimeException.class)
    public void insertAdministratorWithIncorrectFirstnameTest() {
        Administrator administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                null, "Гуров", null, "Admin with incorrect data");
        insertUser(administrator);
    }

    @Test(expected = RuntimeException.class)
    public void updateAdministratorWithIncorrectFirstnameTest() {
        Administrator administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");

        try {
            insertUser(administrator);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        administrator.setFirstName(null);
        updateUser(administrator, administrator.getPassword());
    }

    @Test(expected = RuntimeException.class)
    public void insertAdministratorWithIncorrectLastnameTest() {
        Administrator administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", null, null, "Admin with incorrect data");
        insertUser(administrator);
    }

    @Test(expected = RuntimeException.class)
    public void updateAdministratorWithIncorrectLastnameTest() {
        Administrator administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");

        try {
            insertUser(administrator);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        administrator.setLastName(null);
        updateUser(administrator, administrator.getPassword());
    }

    @Test(expected = RuntimeException.class)
    public void insertAdministratorWithIncorrectPositionTest() {
        Administrator administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, null);
        insertUser(administrator);
    }

    @Test(expected = RuntimeException.class)
    public void updateAdministratorWithIncorrectPositionTest() {
        Administrator administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");

        try {
            insertUser(administrator);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        administrator.setPosition(null);
        updateUser(administrator, administrator.getPassword());
    }
}