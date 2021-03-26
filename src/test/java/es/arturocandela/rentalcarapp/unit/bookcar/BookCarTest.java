package es.arturocandela.rentalcarapp.unit.bookcar;

import es.arturocandela.rentalcarapp.customtags.UnitTest;
import es.arturocandela.rentalcarapp.model.Car;
import es.arturocandela.rentalcarapp.model.implementation.Booking;
import es.arturocandela.rentalcarapp.model.implementation.User;
import es.arturocandela.rentalcarapp.service.CarFinder;
import es.arturocandela.rentalcarapp.service.CarNotFoundException;
import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.InsertException;
import es.arturocandela.rentalcarapp.usecase.*;
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
@UnitTest
@DisplayName("Unit Tests of Book Car Test")
@ExtendWith(MockitoExtension.class)
public class BookCarTest {

    //@BeforeEach
    //public void beforeEach()
    //{
    //    MockitoAnnotations.initMocks(this)  ;
    //}

    @Mock(lenient = true)
    User userStub;

    @Mock(lenient = true)
    DBConnection dbConnection;

    @Mock(lenient = true)
    Car carStub;

    @Mock(lenient = true)
    CarFinder carFinderStub;

    /**
     * Escenario Éxito: Un usuario adulto inicia el proceso de reserva de un coche disponible.
     * El escenario se llevará a cabo correctamente.
     * El Caso de Uso devolverá un objeto de tipo Booking (reserva) con el identificador de la misma y los datos del Usuario y el Coche reservado.
     */
    @DisplayName("S.TS.1: Adults can book Available Cars")
    @Test
    public void adultsCanBookAvailableCars() throws BookingException,InsertException {

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

        Booking booking = bookCarUseCase.execute(userStub,1);

        assertTrue(booking instanceof Booking);

    }

    /**
     *
     * Escenario Fallo 1: Un usuario adulto inicia el proceso de reserva de un coche no disponible.
     * El proceso debe fallar de forma controlada ya que solo se pueden reservar coches que estén disponibles.
     * Se esperará una excepción CarNotAvailableException.
     *
     */
    @DisplayName("F.TS.1: Adults cannot book unavailable cars")

    @Test
    public void adultsCantBookUnavailableCars() throws BookingException, InsertException {
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
            Booking booking = bookCarUseCase.execute(userStub,1);
            assertTrue(booking instanceof Booking);
        });

    }


    /**
     * Escenario Fallo 2: Un usuario adulto inicia el proceso de reserva de un coche inexistente.
     * El proceso debe fallar de forma controlada, se espera una excepción del tipo CarNotFoundException.
     */
    @DisplayName("F.TS.2: Adults cannot book non existent cars")
    @Test
    public void adultsCantBookNonExistentCars() throws BookingException, InsertException
    {
        assertNotNull(userStub);
        when(userStub.getId()).thenReturn(1);
        when(userStub.isAnAdult()).thenReturn(true);

        assertNotNull(dbConnection);
        when(dbConnection.insert(anyString())).thenReturn(1);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(null);

        BookCar bookCarUseCase = new BookCar( carFinderStub , dbConnection );

        assertThrows(CarNotFoundException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            assertTrue(booking instanceof Booking);
        });

    }

    /**
     * Escenario Fallo 3: Un usuario menor de edad inicia el proceso de reserva de un coche disponible.
     * El proceso deberá terminar con una excepción de tipo MinorsCannotBookCarsException
     *
     */
    @DisplayName("F.TS.3: Minors cannot book Available Cars")
    @Test
    public void minorsCannotBookAvailableCars() throws BookingException, InsertException
    {
        assertNotNull(userStub);
        when(userStub.getId()).thenReturn(1);
        when(userStub.isAnAdult()).thenReturn(false);

        assertNotNull(dbConnection);
        when(dbConnection.insert(anyString())).thenReturn(1);

        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(true);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookCar bookCarUseCase = new BookCar( carFinderStub , dbConnection );

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            assertTrue(booking instanceof Booking);
        });
    }

    /**
     * Escenario Fallo 4: Un usuario menor de edad inicia el proceso de reserva de un coche no disponible. El
     * proceso deberá terminar con una excepción de tipo MinorsCannotBookCarsException
     */
    @DisplayName("F.TS.4: Minors cannot book Unvailable Cars")
    @Test
    public void minorsCannotBookUnvailableCars() throws BookingException,InsertException
    {
        assertNotNull(userStub);
        when(userStub.getId()).thenReturn(1);
        when(userStub.isAnAdult()).thenReturn(false);

        assertNotNull(dbConnection);
        when(dbConnection.insert(anyString())).thenReturn(1);

        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(false);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookCar bookCarUseCase = new BookCar( carFinderStub , dbConnection );

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            assertTrue(booking instanceof Booking);
        });

    }

    /**
     * Escenario Fallo 5: Un usuario menor de edad inicia el proceso de reserva de un coche inexistente. El proceso
     * deberá terminar con una excepción de tipo MinorsCannotBookCarsException
     */
    @DisplayName("F.TS.5: Minors cannot book NonExistent Cars")
    @Test
    public void minorsCannotBookNonExistentCars() throws BookingException, InsertException
    {
        assertNotNull(userStub);
        when(userStub.getId()).thenReturn(1);
        when(userStub.isAnAdult()).thenReturn(false);

        assertNotNull(dbConnection);
        when(dbConnection.insert(anyString())).thenReturn(1);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(null);

        BookCar bookCarUseCase = new BookCar( carFinderStub , dbConnection );

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            assertTrue(booking instanceof Booking);
        });
    }
}
