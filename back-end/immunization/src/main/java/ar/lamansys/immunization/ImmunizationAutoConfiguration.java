package ar.lamansys.immunization;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "ar.lamansys.immunization")
@PropertySource(value = "classpath:immunization.properties", ignoreResourceNotFound = true)
public class ImmunizationAutoConfiguration {
}
