package net.pladema.configuration;

import ar.lamansys.odontology.EnableOdontology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableOdontology
@ConditionalOnProperty(
        value="app.feature.HABILITAR_ODONTOLOGY",
        havingValue = "true")
public class OdontologyConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(OdontologyConfiguration.class);

    @PostConstruct
    void started() {
        LOG.info("{}", "Odontology configuration load");
    }
}
