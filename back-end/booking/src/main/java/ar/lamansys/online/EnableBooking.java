package ar.lamansys.online;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(BookingAutoConfiguration.class)
@Configuration
public @interface EnableBooking {
}
