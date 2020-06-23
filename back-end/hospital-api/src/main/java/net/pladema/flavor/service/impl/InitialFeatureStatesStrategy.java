package net.pladema.flavor.service.impl;

import net.pladema.featureflags.service.domain.FlavorBo;
import net.pladema.flavor.features.ChacoGeriatricsFeatureStates;
import net.pladema.flavor.features.HospitalsFeatureStates;
import net.pladema.flavor.features.TandilGeriatricsFeatureStates;
import net.pladema.sgx.featureflags.states.InitialFeatureStates;

public class InitialFeatureStatesStrategy {
	private InitialFeatureStatesStrategy() {
		// utility class
	}

	public static InitialFeatureStates forFlavor(FlavorBo flavor) {
		switch (flavor) {
			case TANDIL: return new TandilGeriatricsFeatureStates();
			case CHACO: return new ChacoGeriatricsFeatureStates();
			case HOSPITALES: return new HospitalsFeatureStates();
		}
		throw new RuntimeException(String.format("Missing InitialFeatureStates for flavor %s", flavor));
	}

}
