package es.arturocandela.rentalcarapp.usecase;

public class UserPersistanceException extends Exception{

    public UserPersistanceException()
    {
        super();
    }

    public UserPersistanceException(Exception e)
    {
        super(e);
    }

    public UserPersistanceException(String message)
    {
        super(message);
    }

}
