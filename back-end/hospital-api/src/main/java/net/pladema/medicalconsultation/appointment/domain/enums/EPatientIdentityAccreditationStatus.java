package net.pladema.medicalconsultation.appointment.domain.enums;

import lombok.Getter;

@Getter
public enum EPatientIdentityAccreditationStatus {

	VALID(1, "VALID"),
	NOT_GIVEN(2, "NOT_GIVEN");

	private final Short id;

	private final String description;

	EPatientIdentityAccreditationStatus(Integer id, String description) {
		this.id = id.shortValue();
		this.description = description;
	}

}
