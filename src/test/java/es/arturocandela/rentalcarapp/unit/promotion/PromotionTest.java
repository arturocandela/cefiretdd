package es.arturocandela.rentalcarapp.unit.promotion;

import es.arturocandela.rentalcarapp.customtags.UnitTest;
import es.arturocandela.rentalcarapp.model.Promotion;
import es.arturocandela.rentalcarapp.model.exception.*;
import io.cucumber.java.eo.Do;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test Para validar el Sistema de Promociones Por Fechas
 *
 * 1. Una promoción consta de un Rango de Fechas y un Descuento
 * 2. Un Descuento, que es un valor numérico entero que representa un porcentaje
 */
@UnitTest
@DisplayName("Promotions: Se podrán crear Promociones que se usarán posteriormente en el proceso de reservas")
@ExtendWith(MockitoExtension.class)
public class PromotionTest {

    @Test
    @DisplayName("S.TS.01: Un administrador puede crear una Promoción y Asignarle un rango de Fechas dentro\n" +
            "del cual será válida. Dicha promoción marcará un descuento que se aplicará una vez se haga el pago")
    void validPromotionCanBeCreated() throws PromotionException{

        ZoneId defaultZoneId = ZoneId.systemDefault();

        Date startDate = Date.from(LocalDate.now().plusDays(1).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(1+3).atStartOfDay(defaultZoneId).toInstant());
        int discount = 20;

        Promotion promotion = new Promotion(startDate,endDate,discount);

        assertNotNull(promotion);

    }

    @Test
    @DisplayName("S.TS.02: Puede consultarse el periodo de la promoción")
    void promotionPeriodCanBeQueried() throws PromotionException {

        ZoneId defaultZoneId = ZoneId.systemDefault();

        Date startDate = Date.from(LocalDate.now().plusDays(1).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(1+3).atStartOfDay(defaultZoneId).toInstant());
        int discount = 20;

        Promotion promotion = new Promotion(startDate,endDate,discount);

        assertEquals(startDate, promotion.getStartDate());
        assertEquals(endDate, promotion.getEndDate());


    }

    @Test
    @DisplayName("S.TS.03: Puede consultarse el descuento aplicado en la promoción")
    void promotionDiscountCanBeQueried() throws PromotionException {

        ZoneId defaultZoneId = ZoneId.systemDefault();

        Date startDate = Date.from(LocalDate.now().plusDays(1).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(1+3).atStartOfDay(defaultZoneId).toInstant());

        int discount = ThreadLocalRandom.current().nextInt(1, 100); // 100 is not included

        Promotion promotion = new Promotion(startDate,endDate,discount);

        assertEquals(discount,promotion.getDiscount());

    }

    @Test
    @DisplayName("F.TS.01: Se lleva a cabo la creación de una promoción con un Rango de Fechas Negativo.")
    void promotionPeriodCantBeNegative() {

        ZoneId defaultZoneId = ZoneId.systemDefault();

        Date startDate = Date.from(LocalDate.now().plusDays(1+3).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(1).atStartOfDay(defaultZoneId).toInstant());

        int discount = ThreadLocalRandom.current().nextInt(1, 100); // 100 is not included

        assertThrows(NegativePromotionPeriodException.class,()->{

            Promotion promotion = new Promotion(startDate,endDate,discount);

        });

    }

    @Test
    @DisplayName("F.TS.02: Se intenta crear un Rango de Fechas que finaliza en el Pasado")
    void promotionPeriodCantBePast() {

        ZoneId defaultZoneId = ZoneId.systemDefault();

        Date startDate = Date.from(LocalDate.now().minusDays(5).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().minusDays(2).atStartOfDay(defaultZoneId).toInstant());

        int discount = ThreadLocalRandom.current().nextInt(1, 100); // 100 is not included

        assertThrows(PastPeriodException.class,()->{

            Promotion promotion = new Promotion(startDate,endDate,discount);

        });


    }

    @Test
    @DisplayName("F.TS.04: Se intenta crear una Promoción con un Descuento de Cero")
    void promotionDiscountCantBeNegative() throws PromotionException{

        ZoneId defaultZoneId = ZoneId.systemDefault();

        Date startDate = Date.from(LocalDate.now().plusDays(1).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(1+3).atStartOfDay(defaultZoneId).toInstant());
        int discount = -1;

        assertThrows(NegativeDiscountException.class,()->{
            Promotion promotion = new Promotion(startDate,endDate,discount);
        });

    }

    @Test
    @DisplayName("F.TS.04: Se intenta crear una Promoción con un Descuento Negativo")
    void promotionDiscountCantBeZero() {

        ZoneId defaultZoneId = ZoneId.systemDefault();

        Date startDate = Date.from(LocalDate.now().plusDays(1).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(1+3).atStartOfDay(defaultZoneId).toInstant());
        int discount = 0;

        assertThrows(ZeroDiscountException.class,()->{
            Promotion promotion = new Promotion(startDate,endDate,discount);
        });

    }

}
