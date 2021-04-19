package es.arturocandela.rentalcarapp.unit.user;

import es.arturocandela.rentalcarapp.customtags.UnitTest;
import es.arturocandela.rentalcarapp.model.User;
import es.arturocandela.rentalcarapp.unit.commom.doubles.UserDouble;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
@DisplayName("Unit Tests of User Class")
class UserTest {

    private static final int LEGAL_AGE_FOR_DRIVING = 18;
    private static final int MINIMUM_USER_AGE=6;

    @DisplayName("User is an Adult Returns False for Minors")
    @ParameterizedTest(name="A User of age {0} is not an adult")
    @ValueSource(ints = {LEGAL_AGE_FOR_DRIVING-1,6})
    void userIsAnAdultReturnsFalseForMinors(int age) {

        User user = new UserDouble(LEGAL_AGE_FOR_DRIVING-1);
        assertFalse(user.isAnAdult());

    }

    @DisplayName("Is an Adult Returns true for People older than Threshold")
    @ParameterizedTest(name = "A User of age {0} is an adult")
    @ValueSource(ints = {LEGAL_AGE_FOR_DRIVING+1,55,102})
    void IsAnAdultReturnsTrueForPeopleOlderThanThreshold(int age) {
        User user = new UserDouble(age);
        assertTrue(user.isAnAdult());

    }

    @DisplayName("Passenger under Minimum age can't be created")
    @Test
    void PassengerUnderMinimumAgeCantBeCreated() {

        assertThrows(IllegalArgumentException.class, () -> new UserDouble(MINIMUM_USER_AGE - 1));

    }

    @DisplayName("Passenger Over Minimum Age can Be Created")
    @ParameterizedTest(name = "A user of the age {0} can be created")
    @ValueSource(ints = {MINIMUM_USER_AGE+1,MINIMUM_USER_AGE + 20, MINIMUM_USER_AGE + 80})
    void passengerOverMinimumAgeCanBeCreated(int age)
    {
        assertDoesNotThrow(()-> new UserDouble(age));
    }

    @DisplayName("User of Minimum Age can be created")
    @Test
    void UsersOfMinimumAgeCanBeCreated()
    {
        User user = new UserDouble(MINIMUM_USER_AGE);
        assertTrue( user instanceof User );
    }



}