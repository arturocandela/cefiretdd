package es.arturocandela.rentalcarapp.unit.bookcar;

import es.arturocandela.rentalcarapp.customtags.UnitTest;
import es.arturocandela.rentalcarapp.model.Car;
import es.arturocandela.rentalcarapp.model.User;
import es.arturocandela.rentalcarapp.model.implementation.Booking;
import es.arturocandela.rentalcarapp.service.*;
import es.arturocandela.rentalcarapp.usecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * In this test, It is needed the use of
 * MockitoAnnotations.openMocks to grant the use of
 * initializations on each property
 */
@UnitTest
@DisplayName("Unit Tests of Book Car Test")
@ExtendWith(MockitoExtension.class)
public class BookCarTest {




    @Mock(lenient = true)
    Car carStub;

    @Mock(lenient = true)
    CarFinder carFinderStub;

    @Mock(lenient = true)
    IConfirmationNotifier notifier;



    /**
     * Escenario Éxito: Un usuario adulto inicia el proceso de reserva de un coche disponible.
     * El escenario se llevará a cabo correctamente.
     * El Caso de Uso devolverá un objeto de tipo Booking (reserva) con el identificador de la misma y los datos del Usuario y el Coche reservado.
     */
    @DisplayName("S.TS.1: Adults can book Available Cars")
    @Test
    public void adultsCanBookAvailableCars() throws BookingException,InsertException {

        User userStub = mock(User.class);
        assertNotNull(userStub);
        when(userStub.isAnAdult()).thenReturn(true);

        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(true);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookingRepository bookingRepositoryStub = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryStub);
        when(bookingRepositoryStub.bookCar(any(),any())).thenReturn(new Booking(1,userStub,carStub));

        assertNotNull(notifier);

        BookCar bookCarUseCase = new BookCar(carFinderStub,bookingRepositoryStub,notifier);
        Booking booking = bookCarUseCase.execute(userStub,1);

        verify(notifier,times(1)).send(any());
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

        User userStub = mock(User.class);
        assertNotNull(userStub);
        when(userStub.isAnAdult()).thenReturn(true);

        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(false);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookingRepository bookingRepositoryDummy = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryDummy);

        assertNotNull(notifier);

        BookCar bookCarUseCase = new BookCar(carFinderStub,bookingRepositoryDummy,notifier);


        assertThrows(CarNotAvailableException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            verify(notifier,times(0)).send(any());
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

        User userStub = mock(User.class);
        assertNotNull(userStub);
        when(userStub.isAnAdult()).thenReturn(true);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenThrow(CarNotFoundException.class);

        BookingRepository bookingRepositoryDummy = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryDummy);

        assertNotNull(notifier);

        BookCar bookCarUseCase = new BookCar(carFinderStub,bookingRepositoryDummy,notifier);

        assertThrows(CarNotFoundException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            verify(notifier,times(0)).send(any());
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
        User userStub = mock(User.class);
        assertNotNull(userStub);
        when(userStub.isAnAdult()).thenReturn(false);

        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(true);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookingRepository bookingRepositoryDummy = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryDummy);

        assertNotNull(notifier);

        BookCar bookCarUseCase = new BookCar(carFinderStub,bookingRepositoryDummy,notifier);

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            verify(notifier,times(0)).send(any());
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

        User userStub = mock(User.class);
        assertNotNull(userStub);
        when(userStub.isAnAdult()).thenReturn(false);

        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(false);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookingRepository bookingRepositoryDummy = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryDummy);

        assertNotNull(notifier);

        BookCar bookCarUseCase = new BookCar(carFinderStub,bookingRepositoryDummy,notifier);

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            verify(notifier,times(0)).send(any());
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

        User userStub = mock(User.class);
        assertNotNull(userStub);
        when(userStub.isAnAdult()).thenReturn(false);

        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(null);

        BookingRepository bookingRepositoryDummy = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryDummy);

        assertNotNull(notifier);

        BookCar bookCarUseCase = new BookCar(carFinderStub,bookingRepositoryDummy,notifier);

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,0);
            verify(notifier,times(0)).send(any());
            assertTrue(booking instanceof Booking);
        });
    }
}
