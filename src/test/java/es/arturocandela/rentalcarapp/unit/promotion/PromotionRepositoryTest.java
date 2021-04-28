package es.arturocandela.rentalcarapp.unit.promotion;

import es.arturocandela.rentalcarapp.model.Promotion;
import es.arturocandela.rentalcarapp.model.exception.PromotionException;
import es.arturocandela.rentalcarapp.service.PromotionRepository;
import es.arturocandela.rentalcarapp.unit.common.promotion.PromotionMother;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PromotionRepositoryTest {

    @Test
    void promotionCanBeSaved() throws PromotionException
    {

        Promotion promotionToSave = PromotionMother.RandomValidPromotion();

        PromotionRepository repository = new PromotionRepository();
        Promotion promotion = repository.save(promotionToSave);

        assertEquals(promotionToSave.getStartDate(),promotion.getStartDate());
        assertEquals(promotionToSave.getEndDate(),promotion.getEndDate());
        assertEquals(promotionToSave.getDiscount(),promotion.getDiscount());

    }

}
