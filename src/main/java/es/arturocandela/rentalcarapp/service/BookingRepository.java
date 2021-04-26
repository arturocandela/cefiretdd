package es.arturocandela.rentalcarapp.service;

import es.arturocandela.rentalcarapp.model.Car;
import es.arturocandela.rentalcarapp.model.Booking;
import es.arturocandela.rentalcarapp.model.User;

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

    public void beginTransaction() {
        //Now is usefull only for TDD Pruposes
    }

    public void commitTransaction() {
        //Now is usefull only for TDD Pruposes
    }

    public void rollbackTransaction() {
        //Now is usefull only for TDD Pruposes
    }
}