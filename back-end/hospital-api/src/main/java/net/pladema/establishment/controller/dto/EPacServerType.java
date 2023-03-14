package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EPacServerType {

	CENTRAL_SERVER((short) 1, "Servidor Central"),
	DIAGNOSTIC_CENTER((short) 2, "Centro de diagnóstico"),
	;

	private final Short id;
	private final String description;
}
