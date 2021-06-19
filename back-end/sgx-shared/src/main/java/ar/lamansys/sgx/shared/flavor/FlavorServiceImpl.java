package ar.lamansys.sgx.shared.flavor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.featureflags.states.InitialFeatureStates;

@Service
public class FlavorServiceImpl implements FlavorService {
	private final FlavorBo flavor;

	public FlavorServiceImpl(
			@Value("${app.flavor:minsal}") String flavor
	) {
		this.flavor = FlavorBo.getEnum(flavor);
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
