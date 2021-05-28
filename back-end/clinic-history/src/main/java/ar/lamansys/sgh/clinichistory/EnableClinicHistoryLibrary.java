package ar.lamansys.sgh.clinichistory;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(ClinicHistoryAutoConfiguration.class)
@Configuration
public @interface EnableClinicHistoryLibrary {
}