package net.pladema.emergencycare.service.domain.enums;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;

public enum EEmergencyCareEvolutionNoteType {
	DOCTOR,
	NURSE;

	static public EEmergencyCareEvolutionNoteType fromDocumentType(EDocumentType docType) {
		if (EDocumentType.NURSING_EMERGENCY_CARE_EVOLUTION.equals(docType))
			return NURSE;
		else
			return DOCTOR;
	}
}
