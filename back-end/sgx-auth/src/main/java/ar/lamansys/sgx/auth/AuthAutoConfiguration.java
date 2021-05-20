package ar.lamansys.sgx.auth;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@ComponentScan(basePackages = {"ar.lamansys.sgx.auth"})
@EnableJpaRepositories(basePackages = {"ar.lamansys.sgx.auth"})
@EntityScan(basePackages = {"ar.lamansys.sgx.auth"})
@PropertySource(value = "classpath:authentication.properties", ignoreResourceNotFound = true)
public class AuthAutoConfiguration {
}