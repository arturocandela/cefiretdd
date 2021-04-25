package es.arturocandela.rentalcarapp.unit.common.car;

import es.arturocandela.rentalcarapp.model.Car;

public class CarMother {

    private final static int DEFAULT_ID = 1;
    private final static String DEFAULT_MODEL = "Ibiza";
    private final static String DEFAULT_FUEL = "diesel";


    public static Car GenerateAvailable() {

        return new Car(DEFAULT_ID,DEFAULT_MODEL,DEFAULT_FUEL);

    }

    public static Car GenerateUnAvailableCar()
    {
        return new Car(DEFAULT_ID, DEFAULT_MODEL,DEFAULT_FUEL,false);
    }

    public static Car WithId() {

        return new Car(DEFAULT_ID,DEFAULT_MODEL,DEFAULT_FUEL);

    }
}
