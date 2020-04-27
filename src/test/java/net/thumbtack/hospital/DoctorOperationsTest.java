package net.thumbtack.hospital;

import net.thumbtack.hospital.model.Doctor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

// TODO Добавить тесты
public class DoctorOperationsTest extends BaseTest {
    @Test
    public void insertDoctorTest() {
        String login = "FrolovaBahiya454";
        String password = "10AFVcPBcmbY";
        String firstName = "Бахия";
        String lastName = "Фролова";
        String patronymic = "Геннадиевна";
        String specialty = "Surgeon";
        String cabinet = "306";

        Doctor expectedDoctor = new Doctor(login, password, firstName, lastName, patronymic, cabinet, specialty);

        try {
            Doctor insertedDoctor = insertDoctor(login, password, firstName, lastName, patronymic, cabinet, specialty);
            expectedDoctor.setId(insertedDoctor.getId());

            Assert.assertEquals(expectedDoctor, insertedDoctor);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void removeDoctorTest() {
        String login = "FrolovaBahiya454";
        String password = "10AFVcPBcmbY";
        String firstName = "Бахия";
        String lastName = "Фролова";
        String patronymic = "Геннадиевна";
        String specialty = "Surgeon";
        String cabinet = "306";

        Doctor expectedDoctor = new Doctor(login, password, firstName, lastName, patronymic, cabinet, specialty);

        try {
            Doctor insertedDoctor = insertDoctor(login, password, firstName, lastName, patronymic, cabinet, specialty);
            expectedDoctor.setId(insertedDoctor.getId());

            Assert.assertEquals(expectedDoctor, insertedDoctor);

            doctorDao.removeDoctor(expectedDoctor.getId());

            Doctor removedDoctor = doctorDao.getDoctorById(expectedDoctor.getId());
            Assert.assertNull(removedDoctor);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void getEmptyDoctorsList() {
        List<Doctor> emptyDoctorsList = doctorDao.getAllDoctors();

        Assert.assertTrue(emptyDoctorsList.isEmpty());
    }

    // TODO Добавить тесты нарушающие правила валидации
}