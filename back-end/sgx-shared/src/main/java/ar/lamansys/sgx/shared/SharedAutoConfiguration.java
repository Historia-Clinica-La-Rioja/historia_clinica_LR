package ar.lamansys.sgx.shared;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;


@Configuration
@ComponentScan(basePackages = {"ar.lamansys.sgx.shared"})
@EnableJpaRepositories(basePackages = {"ar.lamansys.sgx.shared"})
@EntityScan(basePackages = {"ar.lamansys.sgx.shared"})
public class SharedAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SharedAutoConfiguration.class);

    @PostConstruct
    public void doLog() {
        LOG.debug("{}", "SharedAutoConfiguration loaded");
    }
}