package es.arturocandela.rentalcarapp.unit.registeruser;

import es.arturocandela.rentalcarapp.customtags.UnitTest;
import es.arturocandela.rentalcarapp.model.User;
import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.InsertException;
import es.arturocandela.rentalcarapp.unit.commom.doubles.UserDouble;
import es.arturocandela.rentalcarapp.usecase.RegisterUser;
import es.arturocandela.rentalcarapp.usecase.UserPersistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@UnitTest
@DisplayName("Unit Tests of Book Car Test")
@ExtendWith(MockitoExtension.class)
public class RegisterUserTest {

    @Mock
    DBConnection dbConnection;

    @InjectMocks
    RegisterUser registerUser;

    @Test
    @DisplayName("Valid User Can Be Registered")
    void validUserCanBeRegistered() throws UserPersistanceException, InsertException {

        Random r = new Random();
        int low = 10;
        int high = 100;

        assertNotNull(dbConnection);
        when(dbConnection.insert(anyString())).thenReturn(r.nextInt(high-low) + low);
        
        assertNotNull(registerUser);
        User user = registerUser.execute(new UserDouble(6));
        assertTrue(user instanceof User);
        
    }

}
