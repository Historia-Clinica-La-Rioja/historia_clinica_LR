package ar.lamansys.sgx.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(AuthAutoConfiguration.class)
@Configuration
public @interface EnableAuth {
}