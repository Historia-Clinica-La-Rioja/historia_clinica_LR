package ar.lamansys.sgx.cubejs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CubejsAutoConfiguration.class)
@Configuration
public @interface EnableCubeJs {
}