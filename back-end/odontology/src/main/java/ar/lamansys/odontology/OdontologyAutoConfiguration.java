package ar.lamansys.odontology;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "ar.lamansys.odontology")
@PropertySource(value = "classpath:odontology.properties", ignoreResourceNotFound = true)
public class OdontologyAutoConfiguration {
}
