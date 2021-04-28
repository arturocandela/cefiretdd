package es.arturocandela.rentalcarapp.usecase;

import es.arturocandela.rentalcarapp.model.Promotion;
import es.arturocandela.rentalcarapp.model.exception.NegativeDiscountException;
import es.arturocandela.rentalcarapp.model.exception.NegativePromotionPeriodException;
import es.arturocandela.rentalcarapp.model.exception.PastPeriodException;
import es.arturocandela.rentalcarapp.model.exception.ZeroDiscountException;
import es.arturocandela.rentalcarapp.service.PromotionRepository;

import java.util.Date;

public class CreatePromotion {

    PromotionRepository repository;
    public CreatePromotion(PromotionRepository repository)
    {
        this.repository=repository;
    }

    public Promotion execute(Date startDate, Date endDate, int discount) throws PastPeriodException, NegativeDiscountException, ZeroDiscountException, NegativePromotionPeriodException {

        return this.repository.save(new Promotion(startDate,endDate,discount));

    }
}
