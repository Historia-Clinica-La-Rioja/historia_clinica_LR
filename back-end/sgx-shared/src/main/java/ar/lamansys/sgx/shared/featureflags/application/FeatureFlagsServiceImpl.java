package ar.lamansys.sgx.shared.featureflags.application;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.togglz.core.manager.FeatureManager;

@Service
public class FeatureFlagsServiceImpl implements FeatureFlagsService {

	private final Logger logger;
	private final FeatureManager featureManager;

	public FeatureFlagsServiceImpl(FeatureManager featureManager) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.featureManager = featureManager;
	}

	public boolean isOn(AppFeature feature) {
		boolean isOn = featureManager.isActive(feature);
		logger.debug("Feature {} is {}", feature, isOn);
		return isOn;
	}

}