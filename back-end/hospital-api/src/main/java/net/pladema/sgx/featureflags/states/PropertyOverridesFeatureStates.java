package net.pladema.sgx.featureflags.states;

import java.util.function.BiConsumer;
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
	public void forEach(BiConsumer<AppFeature, Boolean> action) {
		this.initialFeatureStates.forEach(this.overrideValue(action));
	}

	private BiConsumer<AppFeature, Boolean> overrideValue(BiConsumer<AppFeature, Boolean> action) {
		return (appFeature, value) -> action.accept(appFeature, propertyValue(appFeature, value));
	}

	private boolean propertyValue(AppFeature feature, boolean initialValue) {
		String propertyToOverrideFeatureState = propertyNameFor(feature);
		logger.debug("Looking for property {}", propertyToOverrideFeatureState);
		String propertyValue = environment.getProperty(propertyToOverrideFeatureState);
		if (propertyValue == null) {
			logger.debug("Property {} is not set, using initial value {}", propertyToOverrideFeatureState, initialValue);
			return initialValue;
		}
		logger.info("Property {} set as {}", propertyToOverrideFeatureState, propertyValue);
		boolean finalValue = Boolean.parseBoolean(propertyValue.trim());
		if (finalValue) {
			logger.warn("Feature {} is ENABLED", feature);
		} else {
			logger.warn("Feature {} is DISABLED", feature);
		}
		return finalValue;
	}

	private static String propertyNameFor(AppFeature feature) {
		return String.format("app.feature.%s", feature);
	}
}
