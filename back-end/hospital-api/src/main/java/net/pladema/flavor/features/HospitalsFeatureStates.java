package net.pladema.flavor.features;

import net.pladema.sgx.featureflags.AppFeature;
import net.pladema.sgx.featureflags.states.InitialFeatureStates;

import java.util.EnumMap;

public class HospitalsFeatureStates implements InitialFeatureStates {

	@Override
	public EnumMap<AppFeature, Boolean> getStates() {
		EnumMap<AppFeature, Boolean> map = new EnumMap<>(AppFeature.class);

		map.put(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS, false);
		map.put(AppFeature.MAIN_DIAGNOSIS_REQUIRED, true);
		map.put(AppFeature.RESPONSIBLE_DOCTOR_REQUIRED, true);
		map.put(AppFeature.HABILITAR_CARGA_FECHA_PROBABLE_ALTA, true);

		return map;
	}

}