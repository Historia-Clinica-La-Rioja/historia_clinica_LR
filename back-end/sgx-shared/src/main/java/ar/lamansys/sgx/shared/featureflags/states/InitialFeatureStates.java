package ar.lamansys.sgx.shared.featureflags.states;

import ar.lamansys.sgx.shared.featureflags.AppFeature;

import java.util.EnumMap;
import java.util.Optional;

public interface InitialFeatureStates {
	EnumMap<AppFeature, Boolean> getStates();

	Optional<AppFeature> getAppFeatureByPropertyKey(String propertyKey);
}
