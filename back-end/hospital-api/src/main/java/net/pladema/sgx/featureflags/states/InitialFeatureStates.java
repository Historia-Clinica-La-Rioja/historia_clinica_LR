package net.pladema.sgx.featureflags.states;

import java.util.function.BiConsumer;

import net.pladema.sgx.featureflags.AppFeature;

public interface InitialFeatureStates {
	void forEach(BiConsumer<AppFeature, Boolean> action);
}
