package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EIndicationStatus {

	INDICATED(1, "indicada"),
	SUSPENDED(2, "suspendida"),
	IN_PROGRESS(3, "en progreso"),
	COMPLETED(4, "completada"),
	REJECTED(5, "rechazada");


	private Short id;
	private String value;

	EIndicationStatus(Number id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Short getId() {
		return id;
	}

	public static EIndicationStatus map(Short id) {
		for (EIndicationStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("indication-status-not-exists", String.format("El estado de indicación %s no existe", id));
	}
}
