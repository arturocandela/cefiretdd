package es.arturocandela.rentalcarapp.unit.bookcar;

import es.arturocandela.rentalcarapp.customtags.UnitTest;
import es.arturocandela.rentalcarapp.model.Car;
import es.arturocandela.rentalcarapp.model.User;
import es.arturocandela.rentalcarapp.model.implementation.Booking;
import es.arturocandela.rentalcarapp.service.*;
import es.arturocandela.rentalcarapp.unit.common.car.CarMother;
import es.arturocandela.rentalcarapp.unit.common.user.UserMother;
import es.arturocandela.rentalcarapp.usecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
class BookCarTest {

    @Mock
    User userMock;

    @Mock
    Car carMock;

    @Mock
    CarFinder carFinderMock;

    @Mock
    BookingRepository bookingRepositoryMock;

    @Mock
    IConfirmationNotifier notifierMock;

    @InjectMocks
    BookCar bookCarUseCaseMock;

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
    void adultsCanBookAvailableCars() throws BookingException,InsertException,NotificationFailedException {

        User user = UserMother.generateAdult();

        Car car = CarMother.GenerateAvailable();

        assertNotNull(carFinderMock);
        when(carFinderMock.find(anyInt())).thenReturn(car);

        assertNotNull(bookingRepositoryMock);
        when(bookingRepositoryMock.bookCar(any(),any())).thenReturn(new Booking(1, user, car));

        assertNotNull(notifierMock);

        Booking booking = bookCarUseCaseMock.execute(user, car.getId());

        verify(bookingRepositoryMock,times(1)).beginTransaction();
        verify(bookingRepositoryMock,times(1)).commitTransaction();
        verify(bookingRepositoryMock,never()).rollbackTransaction();

        verify(notifierMock,times(1)).send(any());

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
    void adultsCantBookUnavailableCars() throws BookingException, InsertException {

        User user = UserMother.generateAdult();

        Car unAvailableCar = CarMother.GenerateUnAvailableCar();

        assertNotNull(carFinderMock);
        when(carFinderMock.find(anyInt())).thenReturn(unAvailableCar);

        assertNotNull(bookingRepositoryMock);

        assertNotNull(notifierMock);

        BookCar bookCarUseCase = new BookCar(carFinderMock,bookingRepositoryMock, notifierMock);

        assertThrows(CarNotAvailableException.class,()->{
            Booking booking = bookCarUseCase.execute(user,unAvailableCar.getId());
            checkRepositoryMockWithNoCallsToTransactionMethods(bookingRepositoryMock);
            verify(notifierMock,times(0)).send(any());
            assertTrue(booking instanceof Booking);
        });

    }


    /**
     * Escenario Fallo 2: Un usuario adulto inicia el proceso de reserva de un coche inexistente.
     * El proceso debe fallar de forma controlada, se espera una excepción del tipo CarNotFoundException.
     */
    @DisplayName("F.TS.2: Adults cannot book non existent cars")
    @Test
    void adultsCantBookNonExistentCars() throws BookingException, InsertException
    {

        assertNotNull(userMock);
        when(userMock.isAnAdult()).thenReturn(true);

        assertNotNull(carFinderMock);
        when(carFinderMock.find(anyInt())).thenThrow(CarNotFoundException.class);

        assertNotNull(bookingRepositoryMock);

        assertNotNull(notifierMock);

        assertThrows(CarNotFoundException.class,()->{

            Booking booking = bookCarUseCaseMock.execute(userMock,1);
            checkRepositoryMockWithNoCallsToTransactionMethods(bookingRepositoryMock);
            verify(notifierMock,times(0)).send(any());
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
    void minorsCannotBookAvailableCars() throws BookingException, InsertException
    {
       
        assertNotNull(userMock);
        when(userMock.isAnAdult()).thenReturn(false);

        when(carMock.getId()).thenReturn(1);
        assertNotNull(carMock);

        assertNotNull(carFinderMock);

        assertNotNull(bookingRepositoryMock);

        assertNotNull(notifierMock);

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCaseMock.execute(userMock, carMock.getId());
            verify(notifierMock,times(0)).send(any());
            assertTrue(booking instanceof Booking);
        });
    }

    /**
     * Escenario Fallo 4: Un usuario menor de edad inicia el proceso de reserva de un coche no disponible. El
     * proceso deberá terminar con una excepción de tipo MinorsCannotBookCarsException
     */
    @DisplayName("F.TS.4: Minors cannot book Unvailable Cars")
    @Test
    void minorsCannotBookUnvailableCars() throws BookingException,InsertException
    {

        assertNotNull(userMock);
        when(userMock.isAnAdult()).thenReturn(false);

        assertNotNull(carMock);

        assertNotNull(carFinderMock);

        assertNotNull(bookingRepositoryMock);

        assertNotNull(notifierMock);

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCaseMock.execute(userMock, carMock.getId());
            verify(notifierMock,times(0)).send(any());
            assertTrue(booking instanceof Booking);
        });

    }

    /**
     * Escenario Fallo 5: Un usuario menor de edad inicia el proceso de reserva de un coche inexistente. El proceso
     * deberá terminar con una excepción de tipo MinorsCannotBookCarsException
     */
    @DisplayName("F.TS.5: Minors cannot book NonExistent Cars")
    @Test
    void minorsCannotBookNonExistentCars() throws BookingException, InsertException
    {

        assertNotNull(userMock);
        when(userMock.isAnAdult()).thenReturn(false);

        assertNotNull(carFinderMock);

        assertNotNull(bookingRepositoryMock);

        assertNotNull(notifierMock);

        assertThrows(MinorsCannotBookCarsException.class,()->{
            Booking booking = bookCarUseCaseMock.execute(userMock,0);
            verify(notifierMock,times(0)).send(any());
            assertTrue(booking instanceof Booking);
        });
    }

    @Test
    public void rollBackOnNotificationFail() throws NotificationFailedException, CarNotFoundException, InsertException {

        assertNotNull(userMock);
        when(userMock.isAnAdult()).thenReturn(true);

        assertNotNull(carMock);
        when(carMock.isAvailable()).thenReturn(true);

        assertNotNull(carFinderMock);
        when(carFinderMock.find(anyInt())).thenReturn(carMock);

        assertNotNull(bookingRepositoryMock);
        when(bookingRepositoryMock.bookCar(any(),any())).thenReturn(new Booking(1,userMock,carMock));

        assertNotNull(notifierMock);
        doThrow(NotificationFailedException.class).when(notifierMock).send(any());

        assertThrows(NotificationFailedException.class,()->{
            Booking booking = bookCarUseCaseMock.execute(userMock, carMock.getId());
            checkRepositoryCheckCallToTransactionMethod(bookingRepositoryMock,
                    times(1),
                    never(),times(1));
        });

    }
}
