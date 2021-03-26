package es.arturocandela.rentalcarapp.usecase;

public class CarNotFoundException extends BookingException{

    public CarNotFoundException(){
        super();
    }

    public CarNotFoundException(Exception e)
    {
        super(e);
    }

    public CarNotFoundException(String message) {
        super(message);
    }
}
