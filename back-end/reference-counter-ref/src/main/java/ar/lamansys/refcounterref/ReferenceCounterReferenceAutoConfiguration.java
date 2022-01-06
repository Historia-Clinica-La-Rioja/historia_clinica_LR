package ar.lamansys.refcounterref;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "ar.lamansys.refcounterref")
@EnableJpaRepositories(basePackages = {"ar.lamansys.refcounterref"})
@EntityScan(basePackages = {"ar.lamansys.refcounterref"})
@PropertySource(value = "classpath:reference-counter-reference.properties", ignoreResourceNotFound = true)
public class ReferenceCounterReferenceAutoConfiguration {
}
