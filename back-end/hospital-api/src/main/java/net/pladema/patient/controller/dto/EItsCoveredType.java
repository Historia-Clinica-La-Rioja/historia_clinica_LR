package net.pladema.patient.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EItsCoveredType {

	EMPTY((short) 0, "LA TABLA ESTÁ VACÍA"),
	COVERED((short) 1, "CON COBERTURA"),
	NOT_COVERED((short) 2, "SIN COBERTURA");

	private final Short id;
	private final String description;

	EItsCoveredType(Short id, String description) {
		this.id = id;
		this.description = description;
	}

	public static List<EItsCoveredType> getAll() {
		return Stream.of(EItsCoveredType.values()).collect(Collectors.toList());
	}

	public static EItsCoveredType map(Short id) {
		for (EItsCoveredType e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("its-covered-state-not-exists", String.format("La opción %s no existe", id));
	}
}
