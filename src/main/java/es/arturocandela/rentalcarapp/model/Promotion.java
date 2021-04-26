package es.arturocandela.rentalcarapp.model;

import es.arturocandela.rentalcarapp.model.exception.NegativeDiscountException;
import es.arturocandela.rentalcarapp.model.exception.NegativePromotionPeriodException;
import es.arturocandela.rentalcarapp.model.exception.PastPeriodException;
import es.arturocandela.rentalcarapp.model.exception.ZeroDiscountException;

import java.util.Date;

public class Promotion {

    final Period period;
    Discount discount;

    public Promotion(Date startDate, Date endDate, int percentage) throws
            NegativePromotionPeriodException,
            PastPeriodException,
            NegativeDiscountException,
            ZeroDiscountException {

        this.period = new Period(startDate,endDate);
        this.discount = new Discount(percentage);

    }

    public Date getStartDate() {
        return period.getStartDate();
    }

    public Date getEndDate() {
        return period.getEndDate();
    }

    public int getDiscount() {
        return discount.getPercentage();
    }
}
