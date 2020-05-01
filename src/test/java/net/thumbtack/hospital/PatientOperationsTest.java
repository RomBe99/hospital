package net.thumbtack.hospital;

import net.thumbtack.hospital.model.Patient;
import org.junit.Assert;
import org.junit.Test;

public class PatientOperationsTest extends BaseTest {
    @Test
    public void insertPatientTest1() {
        String login = "VorobevaAltina312";
        String password = "fwZO9tIyAQlf";
        String firstName = "Альтина";
        String lastName = "Воробьёва";
        String patronymic = "Максимовна";
        String email = "loxelek753@homedepinst.com";
        String address = "623621, г. Усть-Камчатск, ул. Лазо, дом 22, квартира 127";
        String phone = "+79139353542";

        Patient expectedPatient = new Patient(login, password, firstName, lastName, patronymic, email, address, phone);

        try {
            Patient insertedPatient = insertPatient(login, password, firstName, lastName, patronymic, email, address, phone);
            expectedPatient.setId(insertedPatient.getId());

            Assert.assertEquals(expectedPatient, insertedPatient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void insertPatientTest2() {
        String login = "BalandinSadin42";
        String password = "7b3FcdkeB6yZ";
        String firstName = "Садин";
        String lastName = "Миронов";
        String patronymic = "";
        String email = "pisid72989@emailhost99.com";
        String address = "679158, г. Сосновый Бор, ул. Чернореченская, дом 65, квартира 475";
        String phone = "89154568711";

        Patient expectedPatient = new Patient(login, password, firstName, lastName, patronymic, email, address, phone);

        try {
            Patient insertedPatient = insertPatient(login, password, firstName, lastName, patronymic, email, address, phone);
            expectedPatient.setId(insertedPatient.getId());

            Assert.assertEquals(expectedPatient, insertedPatient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void insertPatientTest3() {
        String login = "FrolovaBahiya454";
        String password = "10AFVcPBcmbY";
        String firstName = "Бахия";
        String lastName = "Фролова";
        String patronymic = "Геннадиевна";
        String email = "wovela5746@2go-mail.ru";
        String address = "198504, г. Суровикино, ул. 2-й район Морского порта тер, дом 95, квартира 359";
        String phone = "+79733165586";

        Patient expectedPatient = new Patient(login, password, firstName, lastName, patronymic, email, address, phone);

        try {
            Patient insertedPatient = insertPatient(login, password, firstName, lastName, patronymic, email, address, phone);
            expectedPatient.setId(insertedPatient.getId());

            Assert.assertEquals(expectedPatient, insertedPatient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test(expected = RuntimeException.class)
    public void insertWithSameLoginsTest() {
        String login = "FrolovaBahiya454";
        String password = "10AFVcPBcmbY";
        String firstName = "Бахия";
        String lastName = "Фролова";
        String patronymic = "Геннадиевна";
        String email = "wovela5746@2go-mail.ru";
        String address = "198504, г. Суровикино, ул. 2-й район Морского порта тер, дом 95, квартира 359";
        String phone = "+79733165586";

        try {
            insertPatient(login, password, firstName, lastName, patronymic, email, address, phone);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        insertPatient(login, password, firstName, lastName, patronymic, email, address, phone);
    }

    @Test(expected = RuntimeException.class)
    public void removePatientTest() {
        String login = "FrolovaBahiya454";
        String password = "10AFVcPBcmbY";
        String firstName = "Бахия";
        String lastName = "Фролова";
        String patronymic = "Геннадиевна";
        String email = "wovela5746@2go-mail.ru";
        String address = "198504, г. Суровикино, ул. 2-й район Морского порта тер, дом 95, квартира 359";
        String phone = "+79733165586";

        Patient expectedPatient = new Patient(login, password, firstName, lastName, patronymic, email, address, phone);

        try {
            Patient insertedPatient = insertPatient(login, password, firstName, lastName, patronymic, email, address, phone);
            expectedPatient.setId(insertedPatient.getId());

            Assert.assertEquals(expectedPatient, insertedPatient);
            patientDao.removePatient(insertedPatient.getId());

            Patient removedPatient = patientDao.getPatientById(expectedPatient.getId());
            Assert.assertNull(removedPatient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void updatePatientTest1() {
        String login = "VorobevaAltina312";
        String password = "fwZO9tIyAQlf";
        String firstName = "Альтина";
        String lastName = "Воробьёва";
        String patronymic = "Максимовна";
        String email = "loxelek753@homedepinst.com";
        String address = "623621, г. Усть-Камчатск, ул. Лазо, дом 22, квартира 127";
        String phone = "+79139353542";

        Patient expectedPatient = new Patient(login, password, firstName, lastName, patronymic, email, address, phone);

        try {
            Patient insertedPatient = insertPatient(login, password, firstName, lastName, patronymic, email, address, phone);
            expectedPatient.setId(insertedPatient.getId());

            Assert.assertEquals(expectedPatient, insertedPatient);

            expectedPatient.setAddress("399712, г. Тикси, ул. Панфиловцев 2-й пер, дом 11, квартира 271");

            patientDao.updatePatient(expectedPatient);

            Patient updatedPatient = patientDao.getPatientById(expectedPatient.getId());
            Assert.assertEquals(expectedPatient, updatedPatient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void updatePatientTest2() {
        String login = "BalandinSadin42";
        String password = "7b3FcdkeB6yZ";
        String firstName = "Садин";
        String lastName = "Миронов";
        String patronymic = "";
        String email = "pisid72989@emailhost99.com";
        String address = "679158, г. Сосновый Бор, ул. Чернореченская, дом 65, квартира 475";
        String phone = "89154568711";

        Patient expectedPatient = new Patient(login, password, firstName, lastName, patronymic, email, address, phone);

        try {
            Patient insertedPatient = insertPatient(login, password, firstName, lastName, patronymic, email, address, phone);
            expectedPatient.setId(insertedPatient.getId());

            Assert.assertEquals(expectedPatient, insertedPatient);

            expectedPatient.setEmail("wovela5746@2go-mail.com");
            expectedPatient.setPhone("+79897893075");
            expectedPatient.setPatronymic("Матвеевич");

            patientDao.updatePatient(expectedPatient);

            Patient updatedPatient = patientDao.getPatientById(expectedPatient.getId());
            Assert.assertEquals(expectedPatient, updatedPatient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void updatePatientTest3() {
        String login = "FrolovaBahiya454";
        String password = "10AFVcPBcmbY";
        String firstName = "Бахия";
        String lastName = "Фролова";
        String patronymic = "Геннадиевна";
        String email = "wovela5746@2go-mail.ru";
        String address = "198504, г. Суровикино, ул. 2-й район Морского порта тер, дом 95, квартира 359";
        String phone = "+79733165586";

        Patient expectedPatient = new Patient(login, password, firstName, lastName, patronymic, email, address, phone);

        try {
            Patient insertedPatient = insertPatient(login, password, firstName, lastName, patronymic, email, address, phone);
            expectedPatient.setId(insertedPatient.getId());

            Assert.assertEquals(expectedPatient, insertedPatient);

            expectedPatient.setPassword("sDqS1aR5wx78");
            expectedPatient.setLastName("Мамедов");

            patientDao.updatePatient(expectedPatient);

            Patient updatedPatient = patientDao.getPatientById(expectedPatient.getId());
            Assert.assertEquals(expectedPatient, updatedPatient);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }
}
