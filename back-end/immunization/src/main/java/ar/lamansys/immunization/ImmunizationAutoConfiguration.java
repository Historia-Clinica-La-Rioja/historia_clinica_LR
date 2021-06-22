package ar.lamansys.immunization;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"ar.lamansys.immunization"})
@EnableJpaRepositories(basePackages = {"ar.lamansys.immunization"})
@EntityScan(basePackages = {"ar.lamansys.immunization"})
@PropertySource(value = "classpath:immunization.properties", ignoreResourceNotFound = true)
public class ImmunizationAutoConfiguration {
}
