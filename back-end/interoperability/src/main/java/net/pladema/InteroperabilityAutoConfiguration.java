package net.pladema;

import commercialmedication.cache.configuration.EnableCommercialMedicationUpdateSchemaAutoConfiguration;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@EnableCommercialMedicationUpdateSchemaAutoConfiguration
@Configuration
public class InteroperabilityAutoConfiguration {
    private final Logger logger;

    public InteroperabilityAutoConfiguration(SnomedSemantics snomedSemantics) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        logger.debug("SnomedSemantics -> {}", snomedSemantics);
    }
}
