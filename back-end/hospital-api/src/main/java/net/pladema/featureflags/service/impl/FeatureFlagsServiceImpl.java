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

	public boolean isOn(String name) {
		return getAll().stream().filter(f -> f.getKey().equals(name)).findFirst().map(FeatureFlagBo::isActive).orElse(false);
	}

	private List<FeatureFlagBo> getAll() {
		if (allFlags == null)
			reset(FlavorBo.getEnum(configFlavor));
		return allFlags;
	}


	private List<FeatureFlagBo> reset(FlavorBo flavor) {
		allFlags = new ArrayList<>();
		allFlags.add(flag("responsibleDoctorRequired", flavor.anyMatch(FlavorBo.HOSPITALES),
				"Indica si el médico responsable de una internación es obligatorio"));
		
		allFlags.add(flag("habilitarAltaSinEpicrisis", flavor.anyMatch(FlavorBo.TANDIL, FlavorBo.CHACO),
				"Indica si se puede dar de alta una internación sin tener una epicrisis asociada"));

		allFlags.add(flag("mainDiagnosisRequired", flavor.anyMatch(FlavorBo.HOSPITALES),
				"Indica si el diagnostico principal en una internación es obligatorio"));

		return allFlags;
	}

	/**
	 *
	 * @param key         nombre del flag en el archivo de configuración
	 * @param active      valor por defecto del key
	 * @param label      label del flag
	 * @return el flag creado
	 */
	private FeatureFlagBo flag(String key, boolean active, String label) {
		// Si se definió una property para este flag, se utiliza con mas prioridad
		String featureFlagProperty = "sgh.feature-flag." + key;
		boolean isActive = Boolean.parseBoolean(env.getProperty(featureFlagProperty, Boolean.toString(active)));

		FeatureFlagBo flag = new FeatureFlagBo();
		flag.setActive(isActive);
		flag.setKey(key);
		flag.setLabel(label);
		LOG.info("Using {}", flag);
		return flag;
	}

}