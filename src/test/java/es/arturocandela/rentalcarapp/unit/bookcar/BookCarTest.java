package es.arturocandela.rentalcarapp.unit.bookcar;

import es.arturocandela.rentalcarapp.model.implementation.Booking;
import es.arturocandela.rentalcarapp.model.implementation.Car;
import es.arturocandela.rentalcarapp.model.implementation.User;
import es.arturocandela.rentalcarapp.service.CarFinder;
import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.InsertException;
import es.arturocandela.rentalcarapp.usecase.BookCar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BookCarTest {

    @Mock
    User userStub;

    @Mock
    DBConnection dbConnection;

    @Mock
    Car carStub;

    @Mock
    CarFinder carFinderStub;

    @Test
    @ExtendWith(MockitoExtension.class)
    public void adultsCanBookAvailableCars() throws Exception
    {
        assertNotNull(userStub);
        when(userStub.getId()).thenReturn(1);

        assertNotNull(dbConnection);
        when(dbConnection.insert(anyString())).thenReturn(1);

        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(true);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookCar bookCarUseCase = new BookCar( carFinderStub , dbConnection );

        Booking booking = bookCarUseCase.execute(userStub,1);

        assertTrue(booking instanceof Booking);

    }

}
