package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ERuleType {

	CLINICAL_SPECIALTY((short)1, "Especialidad"),
	PRACTICE_PROCEDURE((short)2, "Practica/Procedimiento");

	private final Short id;
	private final String value;

}
