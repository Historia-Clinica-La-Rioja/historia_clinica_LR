package net.pladema.parameterizedform.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

	public static EFormStatus map(Short id) {
		for (EFormStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("form-status-not-exists", String.format("El estado de toma %s no existe", id));
	}

	public EFormStatus getNextState() {
		if (this.equals(DRAFT)) return ACTIVE;
		if (this.equals(ACTIVE)) return INACTIVE;
		else return ACTIVE;
	}

	public static List<Short> getStatus() {
		List<Short> statusId = new ArrayList<>();
		for (EFormStatus e : values()) {
			statusId.add(e.id);
		};
		return statusId;
	}
}
