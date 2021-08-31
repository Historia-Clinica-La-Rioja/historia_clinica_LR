package ar.lamansys.sgh.clinichistory;


import ar.lamansys.sgh.shared.EnableHospitalSharedLibrary;
import ar.lamansys.sgx.shared.EnableSharedLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;


@EnableHospitalSharedLibrary
@EnableSharedLibrary
@Configuration
@ComponentScan(basePackages = {"ar.lamansys.sgh.clinichistory"})
@EnableJpaRepositories(basePackages = {"ar.lamansys.sgh.clinichistory"})
@EntityScan(basePackages = {"ar.lamansys.sgh.clinichistory"})
@PropertySource(value = "classpath:clinic-history.properties", ignoreResourceNotFound = true)
public class ClinicHistoryAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicHistoryAutoConfiguration.class);

    @PostConstruct
    public void doLog() {
        LOG.debug("{}", "ClinicHistoryAutoConfiguration loaded");
    }
}