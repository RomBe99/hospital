package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.user.Administrator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AdministratorDaoTest extends DaoTestApi {
    @Test
    public void loginRootAdminTest() {
        String sessionId = loginRootAdmin();
        logout(sessionId);
    }

    @Test
    public void insertAdministratorWithLoginAndLogoutTest() {
        final var administrator = new Administrator("VorobevaAltina312", "fwZO9tIyAQlf",
                "Альтина", "Воробьёва", "Максимовна", "Test Admin");
        insertUser(administrator);

        final var sessionId = login(administrator.getLogin(), administrator.getPassword());
        logout(sessionId);
    }

    @Test
    public void updateAdministratorTest1() {
        final var administrator = new Administrator("IoanniShershova328", "rKEKaMzcqI9K",
                "Иоанни", "Шершова", "Петровна", "Test Admin");
        insertUser(administrator);

        final var sessionId = login(administrator.getLogin(), administrator.getPassword());

        administrator.setPosition("Very Important Person");
        updateUser(administrator, administrator.getPassword());

        logout(sessionId);
    }

    @Test
    public void updateAdministratorTest2() {
        final var administrator = new Administrator("HoroborIlin727", "QlK14GlXJGf7",
                "Хоробор", "Ильин", "Закирович", "Test Admin");
        insertUser(administrator);

        final var sessionId = login(administrator.getLogin(), administrator.getPassword());
        final var newPassword = "NFLPcdfQy5JM";

        administrator.setFirstName("Кулун");
        administrator.setLastName("Ковалевский");
        administrator.setPatronymic("Артемович");
        administrator.setPosition("Another admin");
        updateUser(administrator, newPassword);

        logout(sessionId);
    }

    @Test
    public void updateAdministratorTest3() {
        final var administrator = new Administrator("LayzaLermontova350", "P6IPEJLd5ktp",
                "Лайза", "Лермонтова", "Тарасовна", "Test Admin");
        insertUser(administrator);

        final var sessionId = login(administrator.getLogin(), administrator.getPassword());
        final var newPassword = "NFLPcdfQy5JM";

        administrator.setPatronymic("");
        updateUser(administrator, newPassword);

        administrator.setPatronymic(null);
        updateUser(administrator, newPassword);

        logout(sessionId);
    }

    @Test
    public void removeAdministratorTest() {
        final var administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Тестовый администратор");
        insertUser(administrator);

        removeUserById(administrator.getId(), administrator.getClass());
    }

    @Test
    public void insertAdministratorsWithSameLoginTest() {
        Administrator administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");

        try {
            insertUser(administrator);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(administrator));
    }

    @Test
    public void insertAdministratorWithIncorrectLoginTest() {
        final var administrator = new Administrator(null, "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");
        Assertions.assertThrows(RuntimeException.class, () -> insertUser(administrator));
    }

    @Test
    public void insertAdministratorWithIncorrectPasswordTest() {
        final var administrator = new Administrator("HendonGurov725", null,
                "Хендон", "Гуров", null, "Admin with incorrect data");
        Assertions.assertThrows(RuntimeException.class, () -> insertUser(administrator));
    }

    @Test
    public void updateAdministratorWithIncorrectPasswordTest() {
        final var administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");

        try {
            insertUser(administrator);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        Assertions.assertThrows(RuntimeException.class, () -> updateUser(administrator, null));
    }

    @Test
    public void insertAdministratorWithIncorrectFirstnameTest() {
        final var administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                null, "Гуров", null, "Admin with incorrect data");
        Assertions.assertThrows(RuntimeException.class, () -> insertUser(administrator));
    }

    @Test
    public void updateAdministratorWithIncorrectFirstnameTest() {
        final var administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");

        try {
            insertUser(administrator);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        administrator.setFirstName(null);
        Assertions.assertThrows(RuntimeException.class, () -> updateUser(administrator, administrator.getPassword()));
    }

    @Test
    public void insertAdministratorWithIncorrectLastnameTest() {
        final var administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", null, null, "Admin with incorrect data");
        Assertions.assertThrows(RuntimeException.class, () -> insertUser(administrator));
    }

    @Test
    public void updateAdministratorWithIncorrectLastnameTest() {
        final var administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");

        try {
            insertUser(administrator);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        administrator.setLastName(null);
        Assertions.assertThrows(RuntimeException.class, () -> updateUser(administrator, administrator.getPassword()));
    }

    @Test
    public void insertAdministratorWithIncorrectPositionTest() {
        final var administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, null);
        Assertions.assertThrows(RuntimeException.class, () -> insertUser(administrator));
    }

    @Test
    public void updateAdministratorWithIncorrectPositionTest() {
        final var administrator = new Administrator("HendonGurov725", "xxqvb1dmuLFI",
                "Хендон", "Гуров", null, "Admin with incorrect data");

        try {
            insertUser(administrator);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        administrator.setPosition(null);
        Assertions.assertThrows(RuntimeException.class, () -> updateUser(administrator, administrator.getPassword()));
    }
}