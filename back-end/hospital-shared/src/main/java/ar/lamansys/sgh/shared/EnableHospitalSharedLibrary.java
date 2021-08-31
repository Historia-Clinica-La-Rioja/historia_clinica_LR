package ar.lamansys.sgh.shared;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(HospitalSharedAutoConfiguration.class)
@Configuration
public @interface EnableHospitalSharedLibrary {
}