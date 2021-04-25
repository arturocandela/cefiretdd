package es.arturocandela.rentalcarapp.model;

public class Car {

    private int id;
    private String model;
    private String fuel;
    private boolean available;

    /**
     * This creates a new car
     *
     * @param id Car Identifier (index of database)
     * @param model Model of the car
     * @param fuel String that represents the car
     * @param available if it is available for booking or not
     */
    public Car(int id, String model, String fuel, boolean available)
    {
        this.id = id;
        this.model = model;
        this.fuel = fuel;
        this.available = available;
    }

    /**
     * This creates a new car available for booking
     *
     * @param id Car Identifier (index of database)
     * @param model Model of the car
     * @param fuel String that represents the car
     */
    public Car(int id, String model, String fuel)
    {
        this(id,model,fuel,true);
    }

    public int getId() {
        return id;
    }
    public boolean isAvailable(){
        return available;
    }
}


