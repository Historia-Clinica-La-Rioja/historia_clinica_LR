package net.pladema.flavor.features;

import java.util.EnumMap;

import net.pladema.sgx.featureflags.AppFeature;
import net.pladema.sgx.featureflags.states.InitialFeatureStates;

public class HospitalsFeatureStates implements InitialFeatureStates {

	@Override
	public EnumMap<AppFeature, Boolean> getStates() {
		EnumMap<AppFeature, Boolean> map = new EnumMap<>(AppFeature.class);

		map.put(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS, false);
		map.put(AppFeature.MAIN_DIAGNOSIS_REQUIRED, true);
		map.put(AppFeature.RESPONSIBLE_DOCTOR_REQUIRED, true);

		return map;
	}

}