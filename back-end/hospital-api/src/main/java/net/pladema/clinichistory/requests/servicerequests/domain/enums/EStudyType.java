package net.pladema.clinichistory.requests.servicerequests.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EStudyType {
	ROUTINE((short) 1),
	URGENT((short) 2);

	private final Short id;

	EStudyType(Short id) {
		this.id = id;
	}

	public static EStudyType map(Short id) {
		for (EStudyType e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("study-type-not-exists", String.format("El tipo de estudio %s no es válido", id));
	}
}
