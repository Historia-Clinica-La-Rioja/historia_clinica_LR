package net.pladema.medicalconsultation.appointment.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
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

	public static EPatientIdentityAccreditationStatus map(Short id) {
		for (EPatientIdentityAccreditationStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("status-not-exists", String.format("El estado %s no existe", id));
	}

}
