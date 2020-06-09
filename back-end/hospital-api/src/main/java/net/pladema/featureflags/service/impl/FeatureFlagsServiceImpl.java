package net.pladema.featureflags.service.impl;

import net.pladema.featureflags.service.domain.FeatureFlagBo;
import net.pladema.featureflags.service.domain.FlavorBo;
import net.pladema.featureflags.service.FeatureFlagsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeatureFlagsServiceImpl implements FeatureFlagsService {

	private static final Logger LOG = LoggerFactory.getLogger(FeatureFlagsServiceImpl.class);

	private List<FeatureFlagBo> publicFlags;

	private List<FeatureFlagBo> allFlags;

	private final String configFlavor;

	private final Environment env;

	public FeatureFlagsServiceImpl(Environment env, @Value("${app.flavor}") String configFlavor) {
		this.env = env;
		this.publicFlags = null;
		this.allFlags = null;
		this.configFlavor = configFlavor;
	}

	public boolean isOn(String name) {
		return getAll().stream().filter(f -> f.getKey().equals(name)).findFirst().map(FeatureFlagBo::isActive).orElse(false);
	}

	@Override
	public List<FeatureFlagBo> getOnlyPublic() {
		if (publicFlags == null)
			reset(FlavorBo.getEnum(configFlavor));
		return publicFlags;
	}

	@Override
	public List<FeatureFlagBo> getAll() {
		if (allFlags == null)
			reset(FlavorBo.getEnum(configFlavor));
		return allFlags;
	}

	@Override
	public List<FeatureFlagBo> reset(FlavorBo flavor) {
		allFlags = new ArrayList<>();
		allFlags.add(flag("responsibleDoctorRequired", flavor.anyMatch(FlavorBo.HOSPITALES), "Medico responsable requerido ",
				"Indica si el médico responsable de una internación es obligatorio"));
		
		allFlags.add(flag("habilitarAltaSinEpicrisis", flavor.anyMatch(FlavorBo.TANDIL, FlavorBo.CHACO), "Habilitar alta sin epicrisis",
				"Indica si se puede dar de alta una internación sin tener una epicrisis asociada"));

		allFlags.add(flag("mainDiagnosisRequired", flavor.anyMatch(FlavorBo.HOSPITALES), "Diagnostico principal requerido ",
				"Indica si el diagnostico principal en una internación es obligatorio"));

		return allFlags;
	}

	@Override
	/**
	 * Retona true si se pudo cambiar el valor, de otra forma retorna false.
	 */
	public boolean changeState(String key, boolean state) {
		Optional<FeatureFlagBo> flag = this.allFlags.stream().filter(x -> x.getKey().equals(key)).findFirst();

		if (flag.isPresent()) {
			flag.get().setActive(state);
			return true;
		}

		return false;
	}

	/**
	 *
	 * @param key         nombre del flag en el archivo de configuración
	 * @param active      valor por defecto del key
	 * @param nombre      nombre del flag
	 * @param descripcion descripción del flag
	 * @return el flag creado
	 */
	private FeatureFlagBo flag(String key, boolean active, String nombre, String descripcion) {
		// Si se definió una property para este flag, se utiliza con mas prioridad
		String featureFlagProperty = "sgh.feature-flag." + key;
		boolean isActive = Boolean.parseBoolean(env.getProperty(featureFlagProperty, Boolean.toString(active)));

		FeatureFlagBo flag = new FeatureFlagBo();
		flag.setActive(isActive);
		flag.setKey(key);
		flag.setNombre(nombre);
		flag.setDescripcion(descripcion);
		LOG.info("Using {}", flag);
		return flag;
	}

	private static FeatureFlagBo onlyKeyAndActive(FeatureFlagBo featureFlag) {
		FeatureFlagBo flag = new FeatureFlagBo();
		flag.setActive(featureFlag.isActive());
		flag.setKey(featureFlag.getKey());
		return flag;
	}
}