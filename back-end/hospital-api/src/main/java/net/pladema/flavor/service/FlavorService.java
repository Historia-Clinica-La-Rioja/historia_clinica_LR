package net.pladema.flavor.service;

import net.pladema.featureflags.service.domain.FlavorBo;
import net.pladema.sgx.featureflags.states.InitialFeatureStates;


public interface FlavorService {
	FlavorBo getFlavor();

	InitialFeatureStates getFeaturesState();
}
