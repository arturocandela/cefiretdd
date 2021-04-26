package es.arturocandela.rentalcarapp.model;

import es.arturocandela.rentalcarapp.model.exception.NegativeDiscountException;
import es.arturocandela.rentalcarapp.model.exception.ZeroDiscountException;

public class Discount {

    private int percentage;

    public Discount(int percentage) throws NegativeDiscountException, ZeroDiscountException {

        if ( percentage < 0 ){
            throw new NegativeDiscountException();
        }

        if (percentage == 0){
            throw new ZeroDiscountException();
        }

        this.percentage = percentage;

    }

    public int getPercentage() {
        return percentage;
    }
}
