package ar.lamansys.sgh.publicapi;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(HospitalPublicApiAutoConfiguration.class)
@Configuration
public @interface EnableHospitalPublicApi {
}
