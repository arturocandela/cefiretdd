package es.arturocandela.rentalcarapp.service;

import javax.persistence.criteria.CriteriaBuilder;

public class InsertException extends  Exception{

    public InsertException(){
        super();
    }

    public InsertException(Exception e)
    {
        super(e);
    }

    public InsertException(String message)
    {
        super(message);
    }

}
