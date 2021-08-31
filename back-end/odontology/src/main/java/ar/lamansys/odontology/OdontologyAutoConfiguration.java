package ar.lamansys.odontology;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "ar.lamansys.odontology")
@EnableJpaRepositories(basePackages = {"ar.lamansys.odontology"})
@EntityScan(basePackages = {"ar.lamansys.odontology"})
@PropertySource(value = "classpath:odontology.properties", ignoreResourceNotFound = true)
public class OdontologyAutoConfiguration {
}
