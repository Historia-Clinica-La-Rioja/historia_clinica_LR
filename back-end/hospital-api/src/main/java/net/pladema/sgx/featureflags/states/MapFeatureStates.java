package net.pladema.sgx.featureflags.states;

import java.util.EnumMap;
import java.util.function.BiConsumer;

import net.pladema.sgx.featureflags.AppFeature;

public abstract class MapFeatureStates implements InitialFeatureStates {
	private final EnumMap<AppFeature, Boolean> map;

	public MapFeatureStates() {
		this.map = initializeFeatures();
	}

	protected abstract EnumMap<AppFeature, Boolean> initializeFeatures();

	@Override
	public void forEach(BiConsumer<AppFeature, Boolean> action) {
		this.map.forEach(action);
	}
}
