package es.arturocandela.rentalcarapp.service;

public class NonValidConnectionException extends Exception{

    public NonValidConnectionException()
    {
        super();
    }

    public NonValidConnectionException(Exception e)
    {
        super(e);
    }

    public NonValidConnectionException(String message)
    {
        super(message);
    }
}
