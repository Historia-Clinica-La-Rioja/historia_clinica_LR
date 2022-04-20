package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EIndicationType {

	PHARMACO(1, "pharmaco"),
	DIET(2, "diet"),
	PARENTERAL_PLAN(3, "parenteral_plan"),
	OTHER_INDICATION(4, "other_indication");

	private Short id;
	private String value;

	EIndicationType(Number id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Short getId() {
		return id;
	}

	public static EIndicationType map(Short id) {
		for (EIndicationType e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("indication-type-not-exists", String.format("El tipo de indicación %s no existe", id));
	}
}
