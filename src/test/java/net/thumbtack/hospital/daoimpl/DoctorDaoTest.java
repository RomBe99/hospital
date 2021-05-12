package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.user.Doctor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class DoctorDaoTest extends DaoTestApi {
    @Test
    public void insertDoctorTest1() {
        final var doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", Collections.emptyList());

        insertUser(doctor);

        final var sessionId = login(doctor.getLogin(), doctor.getPassword());
        logout(sessionId);
    }

    @Test
    public void insertDoctorTest2() {
        final var doctor = new Doctor("NavazishOrlov929", "3T5FsPoVeUBj",
                "Навазиш", "Орлов", null, "342", "Therapist", Collections.emptyList());

        insertUser(doctor);

        final var sessionId = login(doctor.getLogin(), doctor.getPassword());
        logout(sessionId);
    }

    @Test
    public void removeDoctorByIdTest() {
        final var doctor = new Doctor("KristakiyKazakov579", "79gafrySDbUU",
                "Кристакий", "Казаков", null, "104", "Traumatologist", Collections.emptyList());

        insertUser(doctor);

        removeUserById(doctor.getId(), doctor.getClass());
    }

    @Test
    public void insertDoctorsWithSameLogin() {
        final var doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", "Surgeon", Collections.emptyList());

        try {
            insertUser(doctor);
        } catch (RuntimeException ex) {
            Assertions.fail();
        }

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(doctor));
    }

    @Test
    public void insertDoctorWithIncorrectLogin() {
        final var doctor = new Doctor(null, "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", "Surgeon", Collections.emptyList());

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(doctor));
    }

    @Test
    public void insertDoctorWithIncorrectPassword() {
        final var doctor = new Doctor("MartinKebalka536", null,
                "Martin", "Kebalka", null, "342", "Surgeon", Collections.emptyList());

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(doctor));
    }

    @Test
    public void insertDoctorWithIncorrectFirstname() {
        final var doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                null, "Kebalka", null, "342", "Surgeon", Collections.emptyList());

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(doctor));
    }

    @Test
    public void insertDoctorWithIncorrectLastname() {
        final var doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", null, null, "342", "Surgeon", Collections.emptyList());

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(doctor));
    }

    @Test
    public void insertDoctorWithIncorrectCabinet1() {
        final var doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, null, "Surgeon", Collections.emptyList());

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(doctor));
    }

    @Test
    public void insertDoctorWithIncorrectCabinet2() {
        final var doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "sdafgdsafgdsa", "Surgeon", Collections.emptyList());

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(doctor));
    }

    @Test
    public void insertDoctorWithIncorrectSpeciality1() {
        final var doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", null, Collections.emptyList());

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(doctor));
    }

    @Test
    public void insertDoctorWithIncorrectSpeciality2() {
        final var doctor = new Doctor("MartinKebalka536", "elLxBfbnoDro",
                "Martin", "Kebalka", null, "342", "asdfdfsadg", Collections.emptyList());

        Assertions.assertThrows(RuntimeException.class, () -> insertUser(doctor));
    }
}