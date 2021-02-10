package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.user.Doctor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class DoctorDaoTest extends DaoTestApi {
    @Test
    public void insertDoctorTest1() {
        Doctor doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", Collections.emptyList());

        insertUser(doctor);

        String sessionId = login(doctor.getLogin(), doctor.getPassword());
        logout(sessionId);
    }

    @Test
    public void insertDoctorTest2() {
        Doctor doctor = new Doctor("NavazishOrlov929", "3T5FsPoVeUBj",
                "Навазиш", "Орлов", null, "342", "Therapist", Collections.emptyList());

        insertUser(doctor);

        String sessionId = login(doctor.getLogin(), doctor.getPassword());
        logout(sessionId);
    }

    @Test
    public void removeDoctorByIdTest() {
        Doctor doctor = new Doctor("KristakiyKazakov579", "79gafrySDbUU",
                "Кристакий", "Казаков", null, "104", "Traumatologist", Collections.emptyList());

        insertUser(doctor);

        removeUserById(doctor.getId(), doctor.getClass());
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorsWithSameLogin() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", "Surgeon", Collections.emptyList());

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
                "Martin", "Kebalka", null, "342", "Surgeon", Collections.emptyList());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectPassword() {
        Doctor doctor = new Doctor("MartinKebalka536", null,
                "Martin", "Kebalka", null, "342", "Surgeon", Collections.emptyList());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectFirstname() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                null, "Kebalka", null, "342", "Surgeon", Collections.emptyList());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectLastname() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", null, null, "342", "Surgeon", Collections.emptyList());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectCabinet1() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, null, "Surgeon", Collections.emptyList());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectCabinet2() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "sdafgdsafgdsa", "Surgeon", Collections.emptyList());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectSpeciality1() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", null, Collections.emptyList());
        insertUser(doctor);
    }

    @Test(expected = RuntimeException.class)
    public void insertDoctorWithIncorrectSpeciality2() {
        Doctor doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", "asdfdfsadg", Collections.emptyList());
        insertUser(doctor);
    }
}