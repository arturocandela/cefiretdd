package es.arturocandela.rentalcarapp.usecase;

public class BookingException extends Exception{

    public BookingException(){
        super();
    }

    public BookingException(Exception e)
    {
        super(e);
    }

    public BookingException(String message) {
        super(message);
    }

}
