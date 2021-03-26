package es.arturocandela.rentalcarapp.service;

import es.arturocandela.rentalcarapp.usecase.BookingException;

public class CarNotFoundException extends BookingException {

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
