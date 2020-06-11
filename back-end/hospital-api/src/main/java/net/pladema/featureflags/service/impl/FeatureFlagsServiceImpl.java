package net.pladema.featureflags.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.featureflags.service.domain.FeatureFlagBo;
import net.pladema.featureflags.service.domain.FlavorBo;
import net.pladema.sgx.featureflags.AppFeature;

@Service
public class FeatureFlagsServiceImpl implements FeatureFlagsService {

	private static final Logger LOG = LoggerFactory.getLogger(FeatureFlagsServiceImpl.class);

	private List<FeatureFlagBo> allFlags;

	private final String configFlavor;

	private final Environment env;

	public FeatureFlagsServiceImpl(Environment env, @Value("${app.flavor}") String configFlavor) {
		this.env = env;
		this.allFlags = null;
		this.configFlavor = configFlavor;
	}

	public boolean isOn(AppFeature feature) {
		return getAll().stream()
				.filter(f -> f.key == feature)
				.map(f -> f.active)
				.findFirst()
				.orElse(false);
	}

	private List<FeatureFlagBo> getAll() {
		if (allFlags == null)
			reset(FlavorBo.getEnum(configFlavor));
		return allFlags;
	}


	private List<FeatureFlagBo> reset(FlavorBo flavor) {
		allFlags = new ArrayList<>();
		allFlags.add(flag(AppFeature.RESPONSIBLE_DOCTOR_REQUIRED, flavor.anyMatch(FlavorBo.HOSPITALES)));
		
		allFlags.add(flag(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS, flavor.anyMatch(FlavorBo.TANDIL, FlavorBo.CHACO)));

		allFlags.add(flag(AppFeature.MAIN_DIAGNOSIS_REQUIRED, flavor.anyMatch(FlavorBo.HOSPITALES)));

		return allFlags;
	}

	private FeatureFlagBo flag(AppFeature feature, boolean active) {

		// Si se definió una property para este flag, se utiliza con mas prioridad
		String featureFlagProperty = "togglz.features." + feature.name() + ".enabled";
		boolean isActive = Boolean.parseBoolean(env.getProperty(featureFlagProperty, Boolean.toString(active)));

		FeatureFlagBo flag = new FeatureFlagBo(feature, isActive);
		LOG.info("Using {}", flag);
		return flag;
	}

}