package es.arturocandela.rentalcarapp.usecase;

public class MinorsCannotBookCarsException extends BookingException{

    public MinorsCannotBookCarsException()
    {
        super();
    }

    public MinorsCannotBookCarsException(Exception e)
    {
        super(e);
    }

    public MinorsCannotBookCarsException(String message)
    {
        super(message);
    }

}
