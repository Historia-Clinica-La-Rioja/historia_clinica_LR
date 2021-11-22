package net.pladema;

import ar.lamansys.sgh.clinichistory.EnableClinicHistoryLibrary;
import ar.lamansys.sgx.shared.EnableSharedLibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@Configuration
@EnableClinicHistoryLibrary
@EnableSharedLibrary
@PropertySource(value = "classpath:hospital.properties", ignoreResourceNotFound = true)
@ComponentScan(basePackages = {"net.pladema"})
@EnableJpaRepositories(basePackages = {"net.pladema"})
@EntityScan(basePackages = {"net.pladema"})
public class HospitalLibAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(HospitalLibAutoConfiguration.class);

    @PostConstruct
    public void doLog() {
        LOG.debug("{}", "HospitalLibAutoConfiguration loaded");
    }
}
