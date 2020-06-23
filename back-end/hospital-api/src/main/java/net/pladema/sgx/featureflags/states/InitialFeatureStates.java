package net.pladema.sgx.featureflags.states;

import java.util.EnumMap;

import net.pladema.sgx.featureflags.AppFeature;

public interface InitialFeatureStates {
	EnumMap<AppFeature, Boolean> getStates();
}
