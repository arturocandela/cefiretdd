package es.arturocandela.rentalcarapp.unit.common.promotion;

import es.arturocandela.rentalcarapp.model.Promotion;
import es.arturocandela.rentalcarapp.model.exception.PromotionException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class PromotionMother {

    public static Promotion RandomValidPromotion() throws PromotionException
    {
       int startThreshold = ThreadLocalRandom.current().nextInt(1, 100);
       int endThreshold =  ThreadLocalRandom.current().nextInt(1, 100);

       ZoneId defaultZoneId = ZoneId.systemDefault();
       Date startDate = Date.from(LocalDate.now().plusDays(startThreshold).atStartOfDay(defaultZoneId).toInstant());
       Date endDate = Date.from(LocalDate.now().plusDays(startThreshold+endThreshold).atStartOfDay(defaultZoneId).toInstant());
       int discount = 5;

       return new Promotion(startDate,endDate,discount);
    }

}
