package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.user.Doctor;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class DoctorDaoTest extends DaoTestApi {
    @Test
    public void insertDoctorTest1() {
        Doctor doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", new ArrayList<>());

        insertUser(doctor);

        String sessionId = login(doctor.getLogin(), doctor.getPassword());
        logout(sessionId);
    }

    @Test
    public void insertDoctorTest2() {
        Doctor doctor = new Doctor("NavazishOrlov929", "3T5FsPoVeUBj",
                "Навазиш", "Орлов", null, "342", "Therapist", new ArrayList<>());

        insertUser(doctor);

        String sessionId = login(doctor.getLogin(), doctor.getPassword());
        logout(sessionId);
    }

    @Test
    public void removeDoctorByIdTest() {
        Doctor doctor = new Doctor("KristakiyKazakov579", "79gafrySDbUU",
                "Кристакий", "Казаков", null, "104", "Traumatologist", new ArrayList<>());

        insertUser(doctor);

        removeUserById(doctor.getId(), doctor.getClass());
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorsWithSameLogin() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", "Surgeon", new ArrayList<>());

        try {
            insertUser(doctor);
        } catch (RuntimeException ex) {
            Assert.fail();
        }

        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectLogin() {
        Doctor doctor = new Doctor(null, "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", "Surgeon", new ArrayList<>());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectPassword() {
        Doctor doctor = new Doctor("MartinKebalka536", null,
                "Martin", "Kebalka", null, "342", "Surgeon", new ArrayList<>());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectFirstname() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                null, "Kebalka", null, "342", "Surgeon", new ArrayList<>());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectLastname() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", null, null, "342", "Surgeon", new ArrayList<>());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectCabinet1() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, null, "Surgeon", new ArrayList<>());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectCabinet2() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "sdafgdsafgdsa", "Surgeon", new ArrayList<>());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectSpeciality1() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", null, new ArrayList<>());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectSpeciality2() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", "asdfdfsadg", new ArrayList<>());
        insertUser(doctor);
    }
}