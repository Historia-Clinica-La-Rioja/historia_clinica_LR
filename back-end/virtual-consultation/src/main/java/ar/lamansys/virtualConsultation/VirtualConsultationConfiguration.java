package ar.lamansys.virtualConsultation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "ar.lamansys.virtualConsultation")
@EnableJpaRepositories(basePackages = {"ar.lamansys.virtualConsultation"})
@EntityScan(basePackages = {"ar.lamansys.virtualConsultation"})
@PropertySource(value = "classpath:virtual-consultation.properties", ignoreResourceNotFound = true)
public class VirtualConsultationConfiguration {

	@Value("${jitsi.domain.url}")
	private String jitsiDomainUrlValue;

	public static String JITSI_DOMAIN_URL;

	@Bean
	public void initializeStaticVariable() {
		JITSI_DOMAIN_URL = jitsiDomainUrlValue;
	}

}
