package ar.lamansys.sgx.shared;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

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

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);
        return filter;
    }
}