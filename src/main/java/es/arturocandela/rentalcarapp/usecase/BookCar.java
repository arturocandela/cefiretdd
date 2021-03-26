package es.arturocandela.rentalcarapp.usecase;

import es.arturocandela.rentalcarapp.model.ICar;
import es.arturocandela.rentalcarapp.model.ABooking;
import es.arturocandela.rentalcarapp.model.implementation.User;
import es.arturocandela.rentalcarapp.service.CarFinder;
import es.arturocandela.rentalcarapp.service.DBConnection;
import es.arturocandela.rentalcarapp.service.InsertException;

public class BookCar {

    private CarFinder carFinder;
    private DBConnection dbConnection;

    public BookCar(CarFinder carFinder,DBConnection dbConnection)
    {
        this.carFinder=carFinder;
        this.dbConnection=dbConnection;
    }

    public ABooking execute(User user, int carId) throws CarNotAvailableException, InsertException, MinorsCannotBookCarsException {
        ICar car = carFinder.find(carId);

        if (!user.isAnAdult()){
            throw new MinorsCannotBookCarsException();
        }

        if (!car.isAvailable()){
            throw new CarNotAvailableException(String.format("The car is not available"));
        }

        return bookCar(user,car);

    }

    private ABooking bookCar(User user, ICar car) throws InsertException {
        String sql = String.format("INSERT INTO bookings (userId, carId) " +
                "values(%d,%d)",user.getId(),car.getId());
        return new ABooking(dbConnection.insert(sql),user,car);
    }

}
