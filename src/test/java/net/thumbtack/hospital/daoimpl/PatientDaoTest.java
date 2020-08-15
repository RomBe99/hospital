package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.user.Patient;
import org.junit.Assert;
import org.junit.Test;

public class PatientDaoTest extends DaoTestApi {
    @Test
    public void insertPatientTest1() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", null, "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient);

        String sessionId = login(patient.getLogin(), patient.getPassword());
        logout(sessionId);
    }

    @Test
    public void insertPatientTest2() {
        Patient patient = new Patient("MarikaLevchenko0", "nrusOqHiqsbB",
                "Марика", "Левченко", null, "hzdvmndhtcuraxvchh@ttirv.org",
                "391634, г. Емельяново, ул. Полевой (Усть-Славянка) пер, дом 5, квартира 44", "89340237045");
        insertUser(patient);

        String sessionId = login(patient.getLogin(), patient.getPassword());
        logout(sessionId);
    }

    @Test
    public void updatePatientInformation1() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient);

        String sessionId = login(patient.getLogin(), patient.getPassword());

        patient.setAddress("403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729");
        patient.setLastName("Варфоломеева");
        patient.setPatronymic(null);
        updateUser(patient);

        logout(sessionId);
    }

    @Test
    public void updatePatientInformation2() {
        Patient patient = new Patient("HimenaDovlatova926", "Z9snOza5K2rq",
                "Химена", "Довлатова", "Борисовна", "qiqoplqpzrxenqqlan@ttirv.net",
                "416428, г. Ачхой-Мартан, ул. Набережная (Кунцево), дом 132, квартира 587", "+79249988899");
        insertUser(patient);

        String sessionId = login(patient.getLogin(), patient.getPassword());

        patient.setFirstName("Роуэн");
        patient.setLastName("Сафаров");
        patient.setPatronymic("Игоревич");
        patient.setPassword("DXR5SZEufeD0");
        patient.setEmail("xbowuveyxqgfviwudp@awdrt.net");
        patient.setAddress("662862, г. Мокроус, ул. Люберецкий 2-й проезд, дом 87, квартира 260");
        patient.setPhone("+79488638119");
        updateUser(patient);

        logout(sessionId);
    }

    @Test(expected = RuntimeException.class)
    public void insertPatientWithIncorrectLoginTest() {
        Patient patient = new Patient(null, "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void insertPatientWithIncorrectPasswordTest() {
        Patient patient = new Patient("EallaMerkulova844", null,
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void insertPatientWithIncorrectFirstnameTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                null, "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void insertPatientWithIncorrectLastnameTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", null, "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void insertPatientWithIncorrectEmailTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", null,
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void insertPatientWithIncorrectAddressTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                null, "+79835018633");
        insertUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void insertPatientWithIncorrectPhoneTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", null);
        insertUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void updatePatientWithIncorrectPasswordTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        patient.setPassword(null);
        updateUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void updatePatientWithIncorrectFirstnameTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        patient.setFirstName(null);
        updateUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void updatePatientWithIncorrectLastnameTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        patient.setLastName(null);
        updateUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void updatePatientWithIncorrectEmailTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        patient.setEmail(null);
        updateUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void updatePatientWithIncorrectAddressTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        patient.setAddress(null);
        updateUser(patient);
    }

    @Test(expected = RuntimeException.class)
    public void updatePatientWithIncorrectPhoneTest() {
        Patient patient = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", "Григорьевна", "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");

        try {
            insertUser(patient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        patient.setPhone(null);
        updateUser(patient);
    }
}