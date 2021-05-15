package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.user.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PatientDaoTest extends DaoTestApi {
    @Test
    public void insertPatientTest1() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", null, "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient);

        final var sessionId = login(patient.getLogin(), patient.getPassword());
        logout(sessionId);
    }

    @Test
    public void insertPatientTest2() {
        final var patient = new Patient("MarikaLevchenko0", "nrusOqHiqsbB",
                "Марика", "Левченко", null, "hzdvmndhtcuraxvchh@ttirv.org",
                "391634, г. Емельяново, ул. Полевой (Усть-Славянка) пер, дом 5, квартира 44", "89340237045");
        insertUser(patient);

        final var sessionId = login(patient.getLogin(), patient.getPassword());
        logout(sessionId);
    }

    @Test
    public void updatePatientInformation1() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient);

        final var sessionId = login(patient.getLogin(), patient.getPassword());

        patient.setAddress("403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729");
        patient.setLastName("Варфоломеева");
        patient.setPatronymic(null);
        updateUser(patient, patient.getPassword());

        logout(sessionId);
    }

    @Test
    public void updatePatientInformation2() {
        final var patient = new Patient("HimenaDovlatova926", "Z9snOza5K2rq",
                "Химена", "Довлатова", "Борисовна", "qiqoplqpzrxenqqlan@ttirv.net",
                "416428, г. Ачхой-Мартан, ул. Набережная (Кунцево), дом 132, квартира 587", "+79249988899");
        insertUser(patient);

        String sessionId = login(patient.getLogin(), patient.getPassword());
        String newPassword = "DXR5SZEufeD0";

        patient.setFirstName("Роуэн");
        patient.setLastName("Сафаров");
        patient.setPatronymic("Игоревич");
        patient.setEmail("xbowuveyxqgfviwudp@awdrt.net");
        patient.setAddress("662862, г. Мокроус, ул. Люберецкий 2-й проезд, дом 87, квартира 260");
        patient.setPhone("+79488638119");
        updateUser(patient, newPassword);

        logout(sessionId);
    }

    @Test
    public void insertPatientsWithSameLoginTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(patient));
    }

    @Test
    public void insertPatientWithIncorrectLoginTest() {
        final var patient = new Patient(null, "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(patient));
    }

    @Test
    public void insertPatientWithIncorrectPasswordTest() {
        final var patient = new Patient("EallaMerkulova844", null,
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(patient));
    }

    @Test
    public void insertPatientWithIncorrectFirstnameTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                null, "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(patient));
    }

    @Test
    public void insertPatientWithIncorrectLastnameTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", null, "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(patient));
    }

    @Test
    public void insertPatientWithIncorrectEmailTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", null,
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(patient));

    }

    @Test
    public void insertPatientWithIncorrectAddressTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                null, "+79835018633");

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(patient));
    }

    @Test
    public void insertPatientWithIncorrectPhoneTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", null);

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(patient));
    }

    @Test
    public void updatePatientWithIncorrectPasswordTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        Assertions.assertThrows(RuntimeException.class, () -> updateUser(patient, null));
    }

    @Test
    public void updatePatientWithIncorrectFirstnameTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        patient.setFirstName(null);

        Assertions.assertThrows(RuntimeException.class, () -> updateUser(patient, patient.getPassword()));
    }

    @Test
    public void updatePatientWithIncorrectLastnameTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        patient.setLastName(null);

        Assertions.assertThrows(RuntimeException.class, () -> updateUser(patient, patient.getPassword()));
    }

    @Test
    public void updatePatientWithIncorrectEmailTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        patient.setEmail(null);

        Assertions.assertThrows(RuntimeException.class, () -> updateUser(patient, patient.getPassword()));
    }

    @Test
    public void updatePatientWithIncorrectAddressTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        patient.setAddress(null);

        Assertions.assertThrows(RuntimeException.class, () -> updateUser(patient, patient.getPassword()));
    }

    @Test
    public void updatePatientWithIncorrectPhoneTest() {
        final var patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        patient.setPhone(null);

        Assertions.assertThrows(RuntimeException.class, () -> updateUser(patient, patient.getPassword()));
    }
}