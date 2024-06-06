package net.pladema.snowstorm.services.domain.semantics;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum SnomedECL {
	ALLERGY,
	ANESTHESIA,
	BLOOD_TYPE,
	CARDIOVASCULAR_DISORDER,
	CONSULTATION_REASON,
	DIABETES,
	DIAGNOSIS,
	ELECTROCARDIOGRAPHIC_PROCEDURE,
	EVENT,
	FAMILY_RECORD,
	HOSPITAL_REASON,
	HYPERTENSION,
	MEDICINE_WITH_UNIT_OF_PRESENTATION,
	MEDICINE,
	PERSONAL_RECORD,
	PROCEDURE,
	VACCINE,
	VIOLENCE_MODALITY,
	VIOLENCE_PROBLEM,
	VIOLENCE_TYPE,

	;


	public static SnomedECL map(String id) {
		try {
			return valueOf(id);
		} catch (Exception e) {
			throw new NotFoundException("snomed-ecl-not-exists", String.format("La ecl %s no existe", id));
		}
	}

}
