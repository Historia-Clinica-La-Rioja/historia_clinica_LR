package net.pladema.sgx.featureflags;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.repository.mem.InMemoryStateRepository;
import org.togglz.core.spi.FeatureProvider;

import net.pladema.flavor.service.FlavorService;
import net.pladema.sgx.featureflags.states.InitialFeatureStates;
import net.pladema.sgx.featureflags.states.PropertyOverridesFeatureStates;

@Configuration
public class ToggleConfiguration {

    @Bean
    public FeatureProvider featureProvider() {
        return new EnumBasedFeatureProvider(AppFeature.class);
    }

    @Bean
    public FeatureManager featureManagerBuilder(
            FeatureProvider featureProvider,
            FlavorService flavorService,
            Environment environment
    ) {
        InitialFeatureStates initialFeatureStates = new PropertyOverridesFeatureStates(environment, flavorService.getFeaturesState());
        InMemoryStateRepository stateRepository = new InMemoryStateRepository();

        initialFeatureStates.getStates()
                .entrySet()
                .stream()
                .map(entry -> new FeatureState(entry.getKey(), entry.getValue()))
                .forEach(stateRepository::setFeatureState);

        return new FeatureManagerBuilder()
                .featureProvider(featureProvider)
                .stateRepository(stateRepository)
                .build();

    }
}