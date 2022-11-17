package ar.lamansys.sgx.shared;


import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.AppNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan(basePackages = {"ar.lamansys.sgx.shared"})
@EnableJpaRepositories(basePackages = {"ar.lamansys.sgx.shared"})
@EntityScan(basePackages = {"ar.lamansys.sgx.shared"})
@PropertySource(value = "classpath:sgx_shared.properties", ignoreResourceNotFound = true)
public class SharedAutoConfiguration {

    @PostConstruct
    public void doLog() {
        log.debug("{}", "SharedAutoConfiguration loaded");
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

	@Bean
	public AppNode appNode() {
		return new AppNode(
				UUID.randomUUID().toString()
		);
	}
}