package ar.lamansys.sgh.shared;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;


@Configuration
@ComponentScan(basePackages = {"ar.lamansys.sgh.shared"})
@EnableJpaRepositories(basePackages = {"ar.lamansys.sgh.shared"})
@EntityScan(basePackages = {"ar.lamansys.sgh.shared"})
@PropertySource(value = "classpath:hospital-shared.properties", ignoreResourceNotFound = true)
public class HospitalSharedAutoConfiguration {

	@Value("${jitsi.domain.url:}")
	private String jitsiDomainUrlValue;

	public static String JITSI_DOMAIN_URL;

	@Bean
	public void initializeStaticVariable() {
		JITSI_DOMAIN_URL = jitsiDomainUrlValue;
	}

    private static final Logger LOG = LoggerFactory.getLogger(HospitalSharedAutoConfiguration.class);

    @PostConstruct
    public void doLog() {
        LOG.debug("{}", "ClinicHistoryAutoConfiguration loaded");
    }
}