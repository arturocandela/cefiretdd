package es.arturocandela.rentalcarapp.service;

import es.arturocandela.rentalcarapp.model.Car;
import es.arturocandela.rentalcarapp.model.implementation.Booking;
import es.arturocandela.rentalcarapp.model.implementation.User;
import es.arturocandela.rentalcarapp.usecase.BookCar;

public class BookingRepository {
    private DBConnection dbConnection;

    public BookingRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Booking bookCar(User user, Car car) throws InsertException {
        String sql = String.format("INSERT INTO bookings (userId, carId) " +
                "values(%d,%d)", user.getId(), car.getId());
        return new Booking(dbConnection.insert(sql), user, car);
    }
}