package es.arturocandela.rentalcarapp.model;

public class Car {

    private int id;
    private String model;
    private String fuel;
    private boolean avilable;

    public Car(int id, String model, String fuel)
    {
        this.id = id;
        this.model = model;
        this.fuel = fuel;
    }

    public int getId() {
        return id;
    }
    public boolean isAvailable(){
        return avilable;
    }
}


