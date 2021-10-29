package ar.lamansys.nursing;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(NursingAutoConfiguration.class)
@Configuration
public @interface EnableNursing {
}
