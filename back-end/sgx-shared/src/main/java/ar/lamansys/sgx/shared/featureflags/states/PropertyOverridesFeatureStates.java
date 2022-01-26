package ar.lamansys.sgx.shared.featureflags.states;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.EnumMap;
import java.util.Optional;

public class PropertyOverridesFeatureStates implements InitialFeatureStates {
	private final Logger logger;
	private final EnumMap<AppFeature, Boolean> overridenFeatureStates;
	private final Environment environment;

	public PropertyOverridesFeatureStates(
			Environment environment,
			InitialFeatureStates initialFeatureStates
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.environment = environment;
		overridenFeatureStates = new EnumMap<>(AppFeature.class);
		initialFeatureStates.getStates().forEach((appFeature, initialValue) ->
				overridenFeatureStates.put(appFeature, propertyValue(appFeature, initialValue))
		);
	}

	@Override
	public EnumMap<AppFeature, Boolean> getStates() {
		return overridenFeatureStates;
	}

	@Override
	public Optional<AppFeature> getAppFeatureByPropertyKey(String propertyKey) {
		if (propertyKey == null)
			return Optional.empty();
		return overridenFeatureStates.keySet().stream()
				.filter(appFeature -> appFeature.propertyNameFor().equals(propertyKey))
				.findFirst();
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
		String propertyToOverrideFeatureState = feature.propertyNameFor();
		logger.debug("Looking for property {}", propertyToOverrideFeatureState);

		Optional<String> propertyValue = Optional.ofNullable(environment.getProperty(propertyToOverrideFeatureState));
		propertyValue.ifPresent(value ->  logger.info("Property {} set as {}", propertyToOverrideFeatureState, value));
		return propertyValue.map(String::trim).map(Boolean::parseBoolean);
	}

}
