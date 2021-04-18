package es.arturocandela.rentalcarapp.usecase;

import es.arturocandela.rentalcarapp.model.Car;
import es.arturocandela.rentalcarapp.model.implementation.Booking;
import es.arturocandela.rentalcarapp.model.implementation.User;
import es.arturocandela.rentalcarapp.service.*;

public class BookCar {

    private CarFinder carFinder;
    private IConfirmationNotifier notifier;
    private BookingRepository bookingRepository;

    public BookCar(CarFinder carFinder,BookingRepository bookingRepository,IConfirmationNotifier notifier)
    {
        this.carFinder=carFinder;
        this.bookingRepository=bookingRepository;
        this.notifier=notifier;

    }

    public Booking execute(User user, int carId) throws BookingException, InsertException {

        if (!user.isAnAdult()){
            throw new MinorsCannotBookCarsException();
        }

        Car car = carFinder.find(carId);

        if (!car.isAvailable()){
            throw new CarNotAvailableException(String.format("The car is not available"));
        }

        Booking booking = bookingRepository.bookCar(user, car);

        notifier.send(booking);

        return booking;

    }

}
