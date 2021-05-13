package ar.lamansys.odontology;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(OdontologyAutoConfiguration.class)
@Configuration
public @interface EnableOdontology {
}
