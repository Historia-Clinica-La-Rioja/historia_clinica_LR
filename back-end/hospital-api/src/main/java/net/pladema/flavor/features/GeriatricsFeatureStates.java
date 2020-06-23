package net.pladema.flavor.features;

import java.util.EnumMap;

import net.pladema.sgx.featureflags.AppFeature;
import net.pladema.sgx.featureflags.states.MapFeatureStates;

public class GeriatricsFeatureStates extends MapFeatureStates {

	@Override
	protected EnumMap<AppFeature, Boolean> initializeFeatures() {
		EnumMap<AppFeature, Boolean> map = new EnumMap<>(AppFeature.class);

		map.put(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS, true);
		map.put(AppFeature.MAIN_DIAGNOSIS_REQUIRED, false);
		map.put(AppFeature.RESPONSIBLE_DOCTOR_REQUIRED, false);

		return map;
	}
}
