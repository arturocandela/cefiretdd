package es.arturocandela.rentalcarapp.unit.bookcar;

import es.arturocandela.rentalcarapp.model.Car;
import es.arturocandela.rentalcarapp.model.User;
import es.arturocandela.rentalcarapp.model.implementation.Booking;
import es.arturocandela.rentalcarapp.service.BookingRepository;
import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.InsertException;
import es.arturocandela.rentalcarapp.unit.common.car.CarMother;
import es.arturocandela.rentalcarapp.unit.common.user.UserMother;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class BookingRepositoryTest {

    @Test
    void bookingGetsCreated() throws InsertException {

        User user = UserMother.WithId();
        Car car = CarMother.WithId();

        DBConnection connectionStub = mock(DBConnection.class);

        when(connectionStub.insert(anyString())).thenReturn(1);

        BookingRepository bookingRepository = new BookingRepository(connectionStub);

        Booking booking = bookingRepository.bookCar(user,car);

        assertNotNull(booking);
    }

}
