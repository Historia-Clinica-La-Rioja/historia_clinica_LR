package ar.lamansys.sgx.shared.flavor;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.featureflags.states.InitialFeatureStates;

@Service
public class FlavorServiceImpl implements FlavorService {
	private final FlavorBo flavor;

	public FlavorServiceImpl(
	) {
		this.flavor = FlavorBo.HOSPITALES;
	}

	@Override
	public FlavorBo getFlavor() {
		return this.flavor;
	}

	@Override
	public InitialFeatureStates getFeaturesState() {
		return InitialFeatureStatesStrategy.forFlavor(this.flavor);
	}

}
