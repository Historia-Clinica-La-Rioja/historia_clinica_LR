package ar.lamansys.sgx.shared.featureflags;

import ar.lamansys.sgx.shared.featureflags.states.InitialFeatureStates;
import ar.lamansys.sgx.shared.featureflags.states.PropertyOverridesFeatureStates;
import ar.lamansys.sgx.shared.flavor.FlavorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.togglz.core.context.StaticFeatureManagerProvider;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.repository.mem.InMemoryStateRepository;
import org.togglz.core.spi.FeatureProvider;

@Configuration
public class ToggleConfiguration {

    public static final String PREFIX_APP_FEATURE_PROPERTY = "app.feature.";

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
        var featureManager = new FeatureManagerBuilder()
                .featureProvider(featureProvider)
                .stateRepository(stateRepository)
                .build();
        StaticFeatureManagerProvider.setFeatureManager(featureManager);
        return featureManager;
    }
}