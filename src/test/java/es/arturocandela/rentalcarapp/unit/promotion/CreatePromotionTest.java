package es.arturocandela.rentalcarapp.unit.promotion;

import es.arturocandela.rentalcarapp.customtags.UseCase;
import es.arturocandela.rentalcarapp.model.Promotion;
import es.arturocandela.rentalcarapp.model.exception.NegativePromotionPeriodException;
import es.arturocandela.rentalcarapp.model.exception.PastPeriodException;
import es.arturocandela.rentalcarapp.model.exception.PromotionException;
import es.arturocandela.rentalcarapp.service.PromotionRepository;
import es.arturocandela.rentalcarapp.usecase.CreatePromotion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UseCase
@ExtendWith(MockitoExtension.class)
public class CreatePromotionTest {

    @Spy
    PromotionRepository promotionRepository = new PromotionRepository();

    @Test
    @DisplayName("S.TS.01: Un administrador puede crear una Promoción y Asignarle un rango de Fechas dentro\n" +
            "del cual será válida. Dicha promoción marcará un descuento que se aplicará una vez se haga el pago")
    void validPromotionCanBeCreated() throws PromotionException
    {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date startDate = Date.from(LocalDate.now().plusDays(1).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(5).atStartOfDay(defaultZoneId).toInstant());
        int discount = 5;

        Promotion promotion;
        CreatePromotion useCase = new CreatePromotion(promotionRepository);

        promotion = useCase.execute(startDate,endDate,discount);

        assertNotNull(promotion);

        verify(promotionRepository,times(1)).save(promotion);
    }

    @Test
    @DisplayName("F.TS.01: Se lleva a cabo la creación de una promoción con un Rango de Fechas Negativo.")
    void canCreatePromotionWithNegativePeriod()
    {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        Date startDate = Date.from(LocalDate.now().plusDays(1+3).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(1).atStartOfDay(defaultZoneId).toInstant());

        int discount = ThreadLocalRandom.current().nextInt(1, 100); // 100 is not included

        CreatePromotion useCase = new CreatePromotion(promotionRepository);

        assertThrows(NegativePromotionPeriodException.class,()->{

            Promotion promotion = useCase.execute(startDate,endDate,discount);

        });
    }

    @Test
    @DisplayName("F.TS.02: Se intenta crear un Rango de Fechas que finaliza en el Pasado")
    void canCreatePromotionWithPastPeriod()
    {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        Date startDate = Date.from(LocalDate.now().minusDays(5).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().minusDays(2).atStartOfDay(defaultZoneId).toInstant());

        int discount = ThreadLocalRandom.current().nextInt(1, 100); // 100 is not included

        CreatePromotion useCase = new CreatePromotion(promotionRepository);

        assertThrows(PastPeriodException.class,()->{

            Promotion promotion = useCase.execute(startDate,endDate,discount);

        });
    }

}
