package es.arturocandela.rentalcarapp.usecase;

import es.arturocandela.rentalcarapp.model.implementation.Booking;
import es.arturocandela.rentalcarapp.model.implementation.Car;
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

    public Booking execute(User user, int carId) throws Exception
    {
        Car car = carFinder.find(carId);

        if (car.isAvailable()){
            throw new Exception();
        }

        return bookCar(user,car);

    }

    private Booking bookCar(User user, Car car) throws InsertException {
        String sql = String.format("INSERT INTO bookings (userId, carId) " +
                "values(%d,%d)",user.getId(),car.getId());
        return new Booking(dbConnection.insert(sql),user,car);
    }

}