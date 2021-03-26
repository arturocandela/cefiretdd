package es.arturocandela.rentalcarapp.unit.bookcar;

import es.arturocandela.rentalcarapp.model.ICar;
import es.arturocandela.rentalcarapp.model.ABooking;
import es.arturocandela.rentalcarapp.model.implementation.User;
import es.arturocandela.rentalcarapp.service.CarFinder;
import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.InsertException;
import es.arturocandela.rentalcarapp.usecase.BookCar;
import es.arturocandela.rentalcarapp.usecase.CarNotAvailableException;
import es.arturocandela.rentalcarapp.usecase.MinorsCannotBookCarsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * In this test, It is needed the use of
 * MockitoAnnotations.openMocks to grant the use of
 * initializations on each property
 */
@DisplayName("Book Car Test")
public class BookCarTest extends BookCarBaseTest{

    //@BeforeEach
    //public void beforeEach()
    //{
    //    MockitoAnnotations.initMocks(this)  ;
    //}

    @Mock
    User userStub;

    @Mock
    DBConnection dbConnection;

    @Mock
    ICar carStub;

    @Mock
    CarFinder carFinderStub;

    @DisplayName("Adults can book Available Cars")
    @Test
    @ExtendWith(MockitoExtension.class)
    public void adultsCanBookAvailableCars() throws InsertException, CarNotAvailableException, MinorsCannotBookCarsException {

        assertNotNull(userStub);
        when(userStub.getId()).thenReturn(1);
        when(userStub.isAnAdult()).thenReturn(true);

        assertNotNull(dbConnection);
        when(dbConnection.insert(anyString())).thenReturn(1);

        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(true);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookCar bookCarUseCase = new BookCar( carFinderStub , dbConnection );

        ABooking ABooking = bookCarUseCase.execute(userStub,1);

        assertTrue(ABooking instanceof ABooking);

    }

    @DisplayName("Adults cant book unavailable cars")
    @Test
    public void adultsCantBookUnavailableCars() throws InsertException, CarNotAvailableException, MinorsCannotBookCarsException {
        assertNotNull(userStub);
        when(userStub.getId()).thenReturn(1);
        when(userStub.isAnAdult()).thenReturn(true);

        assertNotNull(dbConnection);
        when(dbConnection.insert(anyString())).thenReturn(1);

        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(false);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookCar bookCarUseCase = new BookCar( carFinderStub , dbConnection );

        assertThrows(CarNotAvailableException.class,()->{
            ABooking ABooking = bookCarUseCase.execute(userStub,1);
            assertTrue(ABooking instanceof ABooking);
        });

    }

    @DisplayName("Adults cannot book non existent cars")
    @Test
    public void adultsCantBookNonExistentCars()
    {
            fail();
    }

    @DisplayName("Minors cannot book Available Cars")
    @Test
    public void minorsCannotBookAvailableCars()
    {
        fail();
    }

    @DisplayName("Minors cannot book Unvailable Cars")
    @Test
    public void minorsCannotBookUnvailableCars()
    {
        fail();
    }

    @DisplayName("Minors cannot book NonExistent Cars")
    @Test
    public void minorsCannotBookNonExistentCars()
    {
        fail();
    }
}
