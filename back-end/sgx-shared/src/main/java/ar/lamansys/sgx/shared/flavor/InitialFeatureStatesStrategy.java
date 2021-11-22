package ar.lamansys.sgx.shared.flavor;


import ar.lamansys.sgx.shared.featureflags.states.InitialFeatureStates;
import ar.lamansys.sgx.shared.flavor.instances.HospitalsFeatureStates;

public class InitialFeatureStatesStrategy {
	private InitialFeatureStatesStrategy() {
		// utility class
	}

	public static InitialFeatureStates forFlavor(FlavorBo flavor) {
		switch (flavor) {
			case HOSPITALES: return new HospitalsFeatureStates();
		}
		throw new RuntimeException(String.format("Missing InitialFeatureStates for flavor %s", flavor));
	}

}
