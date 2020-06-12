package net.pladema.flavor.service;

import net.pladema.featureflags.service.domain.FlavorBo;
import net.pladema.sgx.featureflags.FlavoredFeatureStates;

public interface FlavorService {
	FlavorBo getFlavor();

	FlavoredFeatureStates getFeaturesState();
}
