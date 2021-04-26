package es.arturocandela.rentalcarapp.model;

import es.arturocandela.rentalcarapp.model.exception.NegativePromotionPeriodException;
import es.arturocandela.rentalcarapp.model.exception.PastPeriodException;

import java.util.Date;

public class Period {
    Date startDate;
    Date endDate;

    public Period(Date startDate, Date endDate) throws PastPeriodException, NegativePromotionPeriodException {

        if (endDate.before((new Date())) ){
            throw new PastPeriodException();
        }

        if ( endDate.before(startDate)){
            throw new NegativePromotionPeriodException();
        }

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}