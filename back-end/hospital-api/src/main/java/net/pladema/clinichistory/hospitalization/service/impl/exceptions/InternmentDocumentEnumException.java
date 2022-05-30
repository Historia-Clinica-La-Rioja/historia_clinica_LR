package net.pladema.clinichistory.hospitalization.service.impl.exceptions;

import lombok.Getter;

@Getter
public enum InternmentDocumentEnumException {
	NULL_REASON,
	INVALID_USER,
	INVALID_ID,
	INVALID_DATE,
	INVALID_INTERNMENT_EPISODE_ID,
	HAVE_MEDICAL_DISCHARGE,
	HAVE_EVOLUTION_NOTE,
	HAVE_EPICRISIS,
	HAVE_PHYSICAL_DISCHARGE
}
