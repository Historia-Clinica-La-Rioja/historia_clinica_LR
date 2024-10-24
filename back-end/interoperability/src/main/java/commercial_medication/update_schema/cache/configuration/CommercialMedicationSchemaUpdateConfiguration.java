package commercial_medication.update_schema.cache.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Conditional(CommercialMedicationUpdateSchemaCondition.class)
@ComponentScan(basePackages = {"commercial_medication.update_schema.cache"})
@EnableJpaRepositories(basePackages = {"commercial_medication.update_schema.cache"})
@EntityScan(basePackages = {"commercial_medication.update_schema.cache"})
@Configuration
public class CommercialMedicationSchemaUpdateConfiguration {
}
