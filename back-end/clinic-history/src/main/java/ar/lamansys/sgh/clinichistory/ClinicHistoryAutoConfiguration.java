package ar.lamansys.sgh.clinichistory;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@ComponentScan(basePackages = {"ar.lamansys.sgh.clinichistory"})
@EnableJpaRepositories(basePackages = {"ar.lamansys.sgh.clinichistory"})
@EntityScan(basePackages = {"ar.lamansys.sgh.clinichistory"})
@PropertySource(value = "classpath:clinic-history.properties", ignoreResourceNotFound = true)
public class ClinicHistoryAutoConfiguration {
}