package ar.lamansys.nursing;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "ar.lamansys.nursing")
@EnableJpaRepositories(basePackages = {"ar.lamansys.nursing"})
@EntityScan(basePackages = {"ar.lamansys.nursing"})
@PropertySource(value = "classpath:nursing.properties", ignoreResourceNotFound = true)
public class NursingAutoConfiguration {
}
