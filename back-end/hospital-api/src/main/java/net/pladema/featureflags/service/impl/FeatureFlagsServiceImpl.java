package net.pladema.featureflags.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.flavor.service.FlavorService;
import net.pladema.sgx.featureflags.AppFeature;

@Service
public class FeatureFlagsServiceImpl implements FeatureFlagsService {

	private final Logger logger;

	private final Map<AppFeature, Boolean> flags;

	public FeatureFlagsServiceImpl(FlavorService flavorService) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.flags = flavorService.getFeaturesState().getStates();
	}

	public boolean isOn(AppFeature feature) {
		boolean isOn = flags.getOrDefault(feature, false);
		logger.debug("Feature {} is {}", feature, isOn);
		return isOn;
	}

}