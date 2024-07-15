package net.pladema.parameter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EParameterType {
	NUMERIC((short) 1, "Numerico"),
	FREE_TEXT((short) 2, "Texto libre"),
	SNOMED_ECL((short) 3, "SNOMED ECL"),
	OPTIONS_LIST((short) 4, "Lista de opciones"),
	;

	private final Short id;
	private final String description;
}
