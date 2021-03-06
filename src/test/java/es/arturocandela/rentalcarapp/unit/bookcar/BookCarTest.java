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
     * Escenario ??xito: Un usuario adulto inicia el proceso de reserva de un coche disponible.
     * El escenario se llevar?? a cabo correctamente.
     * El Caso de Uso devolver?? un objeto de tipo Booking (reserva) con el identificador de la misma y los datos del Usuario y el Coche reservado.
     */
    @DisplayName("S.TS.1: Adults can book Available Cars")
    @Test
    void adultsCanBookAvailableCars() throws BookingException,InsertException,NotificationFailedException {

        assertNotNull(userMock);
        when(userMock.isAnAdult()).thenReturn(true);

        assertNotNull(carMock);
        when(carMock.getId()).thenReturn(1);
        when(carMock.isAvailable()).thenReturn(true);

        assertNotNull(carFinderMock);
        when(carFinderMock.find(anyInt())).thenReturn(carMock);

        assertNotNull(bookingRepositoryMock);
        when(bookingRepositoryMock.bookCar(any(),any())).thenReturn(new Booking(1, userMock, carMock));


        assertNotNull(notifierMock);

        Booking booking = bookCarUseCaseMock.execute(userMock, carMock.getId());

        verify(bookingRepositoryMock,times(1)).beginTransaction();
        verify(bookingRepositoryMock,times(1)).commitTransaction();
        verify(bookingRepositoryMock,never()).rollbackTransaction();

        verify(notifierMock,times(1)).send(any());

        assertTrue(booking instanceof Booking);

    }

    /**
     *
     * Escenario Fallo 1: Un usuario adulto inicia el proceso de reserva de un coche no disponible.
     * El proceso debe fallar de forma controlada ya que solo se pueden reservar coches que est??n disponibles.
     * Se esperar?? una excepci??n CarNotAvailableException.
     *
     */
    @DisplayName("F.TS.1: Adults cannot book unavailable cars")
    @Test
    void adultsCantBookUnavailableCars() throws BookingException, InsertException {

        assertNotNull(userMock);
        when(userMock.isAnAdult()).thenReturn(true);

        assertNotNull(carMock);
        when(carMock.isAvailable()).thenReturn(false);

        assertNotNull(carFinderMock);
        when(carFinderMock.find(anyInt())).thenReturn(carMock);

        assertNotNull(bookingRepositoryMock);

        assertNotNull(notifierMock);

        BookCar bookCarUseCase = new BookCar(carFinderMock,bookingRepositoryMock, notifierMock);

        assertThrows(CarNotAvailableException.class,()->{
            Booking booking = bookCarUseCase.execute(userMock,1);
            checkRepositoryMockWithNoCallsToTransactionMethods(bookingRepositoryMock);
            verify(notifierMock,times(0)).send(any());
            assertTrue(booking instanceof Booking);
        });

    }


    /**
     * Escenario Fallo 2: Un usuario adulto inicia el proceso de reserva de un coche inexistente.
     * El proceso debe fallar de forma controlada, se espera una excepci??n del tipo CarNotFoundException.
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
     * El proceso deber?? terminar con una excepci??n de tipo MinorsCannotBookCarsException
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
     * proceso deber?? terminar con una excepci??n de tipo MinorsCannotBookCarsException
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
     * deber?? terminar con una excepci??n de tipo MinorsCannotBookCarsException
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
