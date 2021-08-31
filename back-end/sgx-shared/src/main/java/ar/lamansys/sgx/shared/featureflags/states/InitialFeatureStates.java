package ar.lamansys.sgx.shared.featureflags.states;

import ar.lamansys.sgx.shared.featureflags.AppFeature;

import java.util.EnumMap;

public interface InitialFeatureStates {
	EnumMap<AppFeature, Boolean> getStates();
}
