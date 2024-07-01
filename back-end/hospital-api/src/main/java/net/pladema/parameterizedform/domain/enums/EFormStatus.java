package net.pladema.parameterizedform.domain.enums;

import lombok.Getter;

@Getter
public enum EFormStatus {

	DRAFT(1, "Borrador"),
	ACTIVE(2, "Activo"),
	INACTIVE(3, "Inactivo")
	;

	private final Short id;
	private final String value;

	EFormStatus(Number id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}
}
