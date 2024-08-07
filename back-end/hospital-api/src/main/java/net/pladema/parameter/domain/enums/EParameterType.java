package net.pladema.parameter.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
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

	public static EParameterType map(Short id) {
		if (id == null)
			return null;
		for(EParameterType e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("parameter-type-not-exist", String.format("El tipo de parámetro  %s no existe", id));
	}

}
