package es.arturocandela.rentalcarapp.service;

import es.arturocandela.rentalcarapp.model.Car;

import javax.inject.Inject;
import java.util.Map;

public class CarFinder {


    private DBConnection connection;

    @Inject
    public CarFinder(DBConnection dbConnection)
    {
        this.connection=dbConnection;
    }

    public Car find(int id) throws CarNotFoundException{

        Map<String,Object> carData = (Map<String, Object>) connection.query(String.format("'SELECT * FROM cars WHERE\n" +
                "car.id = '%d' ",id));
        if (null == carData)
        {
            throw new CarNotFoundException(String.format("The car with the id %d was not found on the database",id));
        }

        return new Car( (int)carData.get("id")
                ,(String)carData.get("model"),
                (String)carData.get("fuel"));
    }
}
