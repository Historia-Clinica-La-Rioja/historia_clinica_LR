package net.pladema.flavor.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import net.pladema.featureflags.service.domain.FlavorBo;
import net.pladema.flavor.service.FlavorService;
import net.pladema.sgx.featureflags.states.InitialFeatureStates;
import net.pladema.sgx.featureflags.states.PropertyOverridesFeatureStates;

@Service
public class FlavorServiceImpl implements FlavorService {
	private final FlavorBo flavor;
	private final InitialFeatureStates featuresState;

	public FlavorServiceImpl(
			@Value("${app.flavor:minsal}") String flavor,
			Environment environment
	) {
		this.flavor = FlavorBo.getEnum(flavor);
		this.featuresState = new PropertyOverridesFeatureStates(
				environment,
				InitialFeatureStatesStrategy.forFlavor(this.flavor)
		);
	}

	@Override
	public FlavorBo getFlavor() {
		return this.flavor;
	}

	@Override
	public InitialFeatureStates getFeaturesState() {
		return this.featuresState;
	}

}
