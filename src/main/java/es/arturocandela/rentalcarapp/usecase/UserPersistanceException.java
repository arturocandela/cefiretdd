package es.arturocandela.rentalcarapp.usecase;

import es.arturocandela.rentalcarapp.model.implementation.User;

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
