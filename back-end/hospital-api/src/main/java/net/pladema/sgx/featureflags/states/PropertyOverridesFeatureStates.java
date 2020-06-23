package net.pladema.sgx.featureflags.states;

import java.util.EnumMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import net.pladema.sgx.featureflags.AppFeature;

public class PropertyOverridesFeatureStates implements InitialFeatureStates {
	private final Logger logger;
	private final InitialFeatureStates initialFeatureStates;
	private final Environment environment;

	public PropertyOverridesFeatureStates(
			Environment environment,
			InitialFeatureStates initialFeatureStates
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.environment = environment;
		this.initialFeatureStates = initialFeatureStates;
	}

	@Override
	public EnumMap<AppFeature, Boolean> getStates() {
		EnumMap<AppFeature, Boolean> overridenFeatureStates = new EnumMap<>(AppFeature.class);
		initialFeatureStates.getStates().forEach((appFeature, initialValue) ->
				overridenFeatureStates.put(appFeature, propertyValue(appFeature, initialValue))
		);
		return overridenFeatureStates;
	}

	private boolean propertyValue(AppFeature feature, boolean initialValue) {
		Optional<Boolean> propertyValue = propertyValueFor(feature);
		if (propertyValue.isPresent()) {
			boolean finalValue = propertyValue.get();
			if (finalValue) {
				logger.warn("Feature {} is ENABLED", feature);
			} else {
				logger.warn("Feature {} is DISABLED", feature);
			}
			return finalValue;
		}

		logger.debug("Feature {} is not overridden, using initial value {}", feature, initialValue);
		return initialValue;

	}

	private Optional<Boolean> propertyValueFor(AppFeature feature) {
		String propertyToOverrideFeatureState = propertyNameFor(feature);
		logger.debug("Looking for property {}", propertyToOverrideFeatureState);

		Optional<String> propertyValue = Optional.ofNullable(environment.getProperty(propertyToOverrideFeatureState));
		propertyValue.ifPresent(value ->  logger.info("Property {} set as {}", propertyToOverrideFeatureState, value));
		return propertyValue.map(String::trim).map(Boolean::parseBoolean);
	}

	private static String propertyNameFor(AppFeature feature) {
		return String.format("app.feature.%s", feature);
	}
}
