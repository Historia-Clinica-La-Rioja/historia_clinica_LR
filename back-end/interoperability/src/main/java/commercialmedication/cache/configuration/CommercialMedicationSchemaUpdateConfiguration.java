package commercialmedication.cache.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Conditional(CommercialMedicationUpdateSchemaCondition.class)
@ComponentScan(basePackages = {"commercialmedication.cache"})
@EnableJpaRepositories(basePackages = {"commercialmedication.cache"})
@EntityScan(basePackages = {"commercialmedication.cache"})
@Configuration
public class CommercialMedicationSchemaUpdateConfiguration {
}
