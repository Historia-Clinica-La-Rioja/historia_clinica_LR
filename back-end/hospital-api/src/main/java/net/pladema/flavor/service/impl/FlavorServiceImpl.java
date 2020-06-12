package net.pladema.flavor.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.pladema.featureflags.service.domain.FlavorBo;
import net.pladema.flavor.service.FlavorService;
import net.pladema.sgx.featureflags.FlavoredFeatureStates;

@Service
public class FlavorServiceImpl implements FlavorService {
	private final FlavorBo flavor;
	private final FlavoredFeatureStates featuresState;

	public FlavorServiceImpl(@Value("${app.flavor:minsal}") String flavor) {
		this.flavor = FlavorBo.getEnum(flavor);
		this.featuresState = FlavoredFeatureStrategy.forFlavor(this.flavor);
	}

	@Override
	public FlavorBo getFlavor() {
		return this.flavor;
	}

	@Override
	public FlavoredFeatureStates getFeaturesState() {
		return this.featuresState;
	}

}
