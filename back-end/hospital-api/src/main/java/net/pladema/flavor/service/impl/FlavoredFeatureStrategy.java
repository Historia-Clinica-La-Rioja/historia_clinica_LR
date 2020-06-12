package net.pladema.flavor.service.impl;

import net.pladema.featureflags.service.domain.FlavorBo;
import net.pladema.flavor.features.ChacoGeriatricsFeatures;
import net.pladema.flavor.features.HospitalsFeatures;
import net.pladema.flavor.features.TandilGeriatricsFeatures;
import net.pladema.sgx.featureflags.FlavoredFeatureStates;

public class FlavoredFeatureStrategy {

	public static FlavoredFeatureStates forFlavor(FlavorBo flavor) {
		switch (flavor) {
			case TANDIL: return new TandilGeriatricsFeatures();
			case CHACO: return new ChacoGeriatricsFeatures();
			case HOSPITALES: return new HospitalsFeatures();
		}
		throw new RuntimeException(String.format("Missing FlavoredFeatureStates for flavor %s", flavor));
	}

}
