package ar.lamansys.sgx.shared;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@ComponentScan(basePackages = {"ar.lamansys.sgx.shared"})
@EnableJpaRepositories(basePackages = {"ar.lamansys.sgx.shared"})
@EntityScan(basePackages = {"ar.lamansys.sgx.shared"})
public class SharedAutoConfiguration {
}