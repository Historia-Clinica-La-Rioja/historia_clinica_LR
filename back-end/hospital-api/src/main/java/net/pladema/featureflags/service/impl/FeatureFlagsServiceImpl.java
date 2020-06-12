package net.pladema.featureflags.service.impl;

import java.util.EnumMap;
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
		this.flags = new EnumMap<>(AppFeature.class);
		flavorService.getFeaturesState().forEach(flags::put);
	}

	public boolean isOn(AppFeature feature) {
		return flags.getOrDefault(feature, false);
	}

}