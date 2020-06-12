package net.pladema.sgx.featureflags;

import java.util.function.BiConsumer;

public interface FlavoredFeatureStates {
	void forEach(BiConsumer<AppFeature, Boolean> action);
}
