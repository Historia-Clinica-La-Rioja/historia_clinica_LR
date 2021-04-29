package net.pladema;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(HospitalLibAutoConfiguration.class)
@Configuration
public @interface EnableHospitalLib {
}
