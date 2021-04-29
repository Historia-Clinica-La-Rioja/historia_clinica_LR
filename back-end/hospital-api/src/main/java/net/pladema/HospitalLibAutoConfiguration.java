package net.pladema;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource(value = "classpath:hospital.properties", ignoreResourceNotFound = true)
@ComponentScan(basePackages = {"net.pladema"})
@EnableJpaRepositories(basePackages = {"net.pladema"})
@EntityScan(basePackages = {"net.pladema"})
public class HospitalLibAutoConfiguration {
}
