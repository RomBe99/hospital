package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Stream;

public class CommonDaoTest extends DaoTestApi {
    @ParameterizedTest
    @MethodSource("getUserTypeByUserIdWithCorrectIdData")
    public void getUserTypeByUserIdWithCorrectIdTest(User user, UserType expectedUserType) {
        insertUser(user);

        final var userId = user.getId();
        final var actualUserType = getUserTypeByUserId(userId);

        Assertions.assertEquals(expectedUserType, actualUserType);
    }

    private static Stream<Arguments> getUserTypeByUserIdWithCorrectIdData() {
        return Stream.of(
                Arguments.arguments(
                        new Administrator("KylieMccalpin345", "qXRf0ujqF501",
                                "Kylie", "Mccalpin", null, "Test admin"),
                        UserType.ADMINISTRATOR
                ),
                Arguments.arguments(
                        new Patient("CasenTurns398", "yC6JZ7YHqd6F",
                                "Casen", "Turns", null, "jgmfqxbdjvzgikyklu@awdrt.net",
                                "461541, г. Выборг, ул. Пименовский туп, дом 159, квартира 414", "+79530654217"),
                        UserType.PATIENT
                ),
                Arguments.arguments(
                        new Doctor("RoudiKarpov62", "EaGV1fBkLxrX",
                                "Роуди", "Карпов", "Артемович", "306", "Therapist", Collections.emptyList()),
                        UserType.DOCTOR
                )
        );
    }
}