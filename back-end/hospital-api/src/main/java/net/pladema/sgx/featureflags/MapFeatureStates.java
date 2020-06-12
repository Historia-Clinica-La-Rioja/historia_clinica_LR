package net.pladema.sgx.featureflags;

import java.util.EnumMap;
import java.util.function.BiConsumer;

public abstract class MapFeatureStates implements FlavoredFeatureStates {
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
