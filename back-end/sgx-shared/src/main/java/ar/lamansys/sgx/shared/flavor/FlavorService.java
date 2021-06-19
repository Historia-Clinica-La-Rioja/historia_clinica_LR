package ar.lamansys.sgx.shared.flavor;

import ar.lamansys.sgx.shared.featureflags.states.InitialFeatureStates;


public interface FlavorService {
	FlavorBo getFlavor();

	InitialFeatureStates getFeaturesState();
}
