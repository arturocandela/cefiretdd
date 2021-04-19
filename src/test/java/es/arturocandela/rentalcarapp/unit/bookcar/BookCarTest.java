package es.arturocandela.rentalcarapp.unit.bookcar;

import es.arturocandela.rentalcarapp.customtags.UnitTest;
import es.arturocandela.rentalcarapp.model.Car;
import es.arturocandela.rentalcarapp.model.User;
import es.arturocandela.rentalcarapp.model.implementation.Booking;
import es.arturocandela.rentalcarapp.service.*;
import es.arturocandela.rentalcarapp.usecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.verification.VerificationMode;

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
public class BookCarTest {

    private void checkRepositoryCheckCallToTransactionMethod(BookingRepository bookingRepositoryMock,
                                                             VerificationMode beginMode,
                                                             VerificationMode commitMode,
                                                             VerificationMode rollbackMode)
    {
        verify(bookingRepositoryMock,beginMode).beginTransaction();
        verify(bookingRepositoryMock,commitMode).commitTransaction();
        verify(bookingRepositoryMock,rollbackMode).rollbackTransaction();
    }

    private void checkRepositoryMockWithNoCallsToTransactionMethods(BookingRepository bookingRepositoryMock)
    {
        checkRepositoryCheckCallToTransactionMethod(bookingRepositoryMock,never(),never(),never());
    }

    /**
     * Escenario Éxito: Un usuario adulto inicia el proceso de reserva de un coche disponible.
     * El escenario se llevará a cabo correctamente.
     * El Caso de Uso devolverá un objeto de tipo Booking (reserva) con el identificador de la misma y los datos del Usuario y el Coche reservado.
     */
    @DisplayName("S.TS.1: Adults can book Available Cars")
    @Test
    public void adultsCanBookAvailableCars() throws BookingException,InsertException,NotificationFailedException {

        User userStub = mock(User.class);
        assertNotNull(userStub);
        when(userStub.isAnAdult()).thenReturn(true);

        Car carStub = mock(Car.class);
        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(true);

        CarFinder carFinderStub = mock(CarFinder.class);
        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookingRepository bookingRepositoryMock = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryMock);
        when(bookingRepositoryMock.bookCar(any(),any())).thenReturn(new Booking(1,userStub,carStub));

        IConfirmationNotifier notifierDummy = mock(IConfirmationNotifier.class);
        assertNotNull(notifierDummy);

        BookCar bookCarUseCase = new BookCar(carFinderStub,bookingRepositoryMock,notifierDummy);
        Booking booking = bookCarUseCase.execute(userStub,1);

        verify(bookingRepositoryMock,times(1)).beginTransaction();
        verify(bookingRepositoryMock,times(1)).commitTransaction();
        verify(bookingRepositoryMock,never()).rollbackTransaction();

        verify(notifierDummy,times(1)).send(any());

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

        Car carStub = mock(Car.class);
        assertNotNull(carStub);
        when(carStub.isAvailable()).thenReturn(false);

        CarFinder carFinderStub = mock(CarFinder.class);
        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookingRepository bookingRepositoryMock = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryMock);

        IConfirmationNotifier notifierDummy = mock(IConfirmationNotifier.class);
        assertNotNull(notifierDummy);

        BookCar bookCarUseCase = new BookCar(carFinderStub,bookingRepositoryMock,notifierDummy);


        assertThrows(CarNotAvailableException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            checkRepositoryMockWithNoCallsToTransactionMethods(bookingRepositoryMock);
            verify(notifierDummy,times(0)).send(any());
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

        CarFinder carFinderStub = mock(CarFinder.class);
        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenThrow(CarNotFoundException.class);

        BookingRepository bookingRepositoryMock = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryMock);

        IConfirmationNotifier notifierDummy = mock(IConfirmationNotifier.class);
        assertNotNull(notifierDummy);

        BookCar bookCarUseCase = new BookCar(carFinderStub,bookingRepositoryMock,notifierDummy);

        assertThrows(CarNotFoundException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            verify(bookingRepositoryMock,never()).beginTransaction();
            verify(bookingRepositoryMock,never()).commitTransaction();
            verify(bookingRepositoryMock,never()).rollbackTransaction();
            verify(notifierDummy,times(0)).send(any());
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

        Car carDummy = mock(Car.class);
        assertNotNull(carDummy);

        CarFinder carFinderDummy = mock(CarFinder.class);
        assertNotNull(carFinderDummy);

        BookingRepository bookingRepositoryDummy = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryDummy);

        IConfirmationNotifier notifierDummy = mock(IConfirmationNotifier.class);
        assertNotNull(notifierDummy);

        BookCar bookCarUseCase = new BookCar(carFinderDummy,bookingRepositoryDummy,notifierDummy);

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            verify(notifierDummy,times(0)).send(any());
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

        Car carStubDummy = mock(Car.class);
        assertNotNull(carStubDummy);

        CarFinder carFinderDummy = mock(CarFinder.class);
        assertNotNull(carFinderDummy);

        BookingRepository bookingRepositoryDummy = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryDummy);

        IConfirmationNotifier notifierDummy = mock(IConfirmationNotifier.class);
        assertNotNull(notifierDummy);

        BookCar bookCarUseCase = new BookCar(carFinderDummy,bookingRepositoryDummy,notifierDummy);

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,1);
            verify(notifierDummy,times(0)).send(any());
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

        CarFinder carFinderDummy = mock(CarFinder.class);
        assertNotNull(carFinderDummy);

        BookingRepository bookingRepositoryDummy = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryDummy);

        IConfirmationNotifier notifierDummy = mock(IConfirmationNotifier.class);
        assertNotNull(notifierDummy);

        BookCar bookCarUseCase = new BookCar(carFinderDummy,bookingRepositoryDummy,notifierDummy);

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub,0);
            verify(notifierDummy,times(0)).send(any());
            assertTrue(booking instanceof Booking);
        });
    }

    @Test
    public void rollBackOnNotificationFail() throws NotificationFailedException, CarNotFoundException, InsertException {
        User userStub = mock(User.class);
        assertNotNull(userStub);
        when(userStub.isAnAdult()).thenReturn(true);

        Car carStub = mock(Car.class);
        assertNotNull(carStub);
        when(carStub.getId()).thenReturn(1);
        when(carStub.isAvailable()).thenReturn(true);

        CarFinder carFinderStub = mock(CarFinder.class);
        assertNotNull(carFinderStub);
        when(carFinderStub.find(anyInt())).thenReturn(carStub);

        BookingRepository bookingRepositoryMock = mock(BookingRepository.class);
        assertNotNull(bookingRepositoryMock);
        when(bookingRepositoryMock.bookCar(any(),any())).thenReturn(new Booking(1,userStub,carStub));

        IConfirmationNotifier notifierStub = mock(IConfirmationNotifier.class);
        assertNotNull(notifierStub);
        doThrow(NotificationFailedException.class).when(notifierStub).send(any());

        BookCar bookCarUseCase = new BookCar(carFinderStub,bookingRepositoryMock,notifierStub);

        assertThrows(NotificationFailedException.class,()->{
            Booking booking = bookCarUseCase.execute(userStub, carStub.getId());
            checkRepositoryCheckCallToTransactionMethod(bookingRepositoryMock,
                    times(1),
                    never(),times(1));
        });

    }
}
