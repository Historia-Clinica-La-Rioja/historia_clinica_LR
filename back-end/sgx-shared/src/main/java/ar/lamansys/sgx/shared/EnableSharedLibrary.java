package ar.lamansys.sgx.shared;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(SharedAutoConfiguration.class)
@Configuration
public @interface EnableSharedLibrary {
}