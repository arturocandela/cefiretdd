package es.arturocandela.rentalcarapp.unit.bookcar;

import es.arturocandela.rentalcarapp.customtags.UnitTest;
import es.arturocandela.rentalcarapp.model.Car;
import es.arturocandela.rentalcarapp.service.CarFinder;
import es.arturocandela.rentalcarapp.service.CarNotFoundException;
import es.arturocandela.rentalcarapp.service.DBConnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@UnitTest
@DisplayName("Unit Tests of Car Finder Test")
@ExtendWith(MockitoExtension.class)
public class CarFinderTest {

    @Mock(lenient = true)
    DBConnection dbConnection;

    @InjectMocks
    CarFinder carFinder;

    @Test
    @DisplayName("Should Return a car If One is found")
    void shouldReturnACarIfOneIsFound() throws CarNotFoundException
    {
        Map<String,Object> fakeReturn = new HashMap<>();
        fakeReturn.put("id",1);
        fakeReturn.put("model","Ibiza");
        fakeReturn.put("fuel","diesel");

        assertNotNull(dbConnection);
        when(dbConnection.query(anyString())).thenReturn(fakeReturn);

        Car car = carFinder.find(1);

        assertEquals(fakeReturn.get("id"),car.getId());
        assertTrue(car instanceof Car);

    }
}
