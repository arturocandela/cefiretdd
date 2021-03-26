package es.arturocandela.rentalcarapp.usecase;

public class CarNotAvailableException extends Exception{

    public CarNotAvailableException()
    {
        super();
    }

    public CarNotAvailableException(Exception e)
    {
        super(e);
    }

    public CarNotAvailableException(String message)
    {
        super(message);
    }
}
