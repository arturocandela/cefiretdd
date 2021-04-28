package es.arturocandela.rentalcarapp.customtags;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Tag("acceptance")
public @interface AcceptanceTest {
}
