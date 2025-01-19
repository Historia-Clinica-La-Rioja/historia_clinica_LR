package snomed.relations.cache.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Conditional(SnomedRelationsCacheCondition.class)
@ComponentScan(basePackages = {"snomed.relations.cache"})
@EnableJpaRepositories(basePackages = {"snomed.relations.cache"})
@EntityScan(basePackages = {"snomed.relations.cache"})
@Configuration
public class SnomedRelationsCacheConfiguration {
}
