package es.arturocandela.rentalcarapp.usecase;

public class MinorsCannotBookCarsException extends Exception{

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
