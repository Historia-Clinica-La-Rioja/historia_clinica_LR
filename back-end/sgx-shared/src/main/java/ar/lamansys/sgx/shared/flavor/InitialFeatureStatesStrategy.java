package ar.lamansys.sgx.shared.flavor;


import ar.lamansys.sgx.shared.featureflags.states.InitialFeatureStates;
import ar.lamansys.sgx.shared.flavor.instances.ChacoGeriatricsFeatureStates;
import ar.lamansys.sgx.shared.flavor.instances.HospitalsFeatureStates;
import ar.lamansys.sgx.shared.flavor.instances.PBAHospitalsFeatureStates;

public class InitialFeatureStatesStrategy {
	private InitialFeatureStatesStrategy() {
		// utility class
	}

	public static InitialFeatureStates forFlavor(FlavorBo flavor) {
		switch (flavor) {
			case CHACO: return new ChacoGeriatricsFeatureStates();
			case HOSPITALES: return new HospitalsFeatureStates();
			case PBA: return new PBAHospitalsFeatureStates();
		}
		throw new RuntimeException(String.format("Missing InitialFeatureStates for flavor %s", flavor));
	}

}
