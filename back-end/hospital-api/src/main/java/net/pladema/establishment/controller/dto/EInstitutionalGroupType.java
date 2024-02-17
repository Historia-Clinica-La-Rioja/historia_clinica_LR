package net.pladema.establishment.controller.dto;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EInstitutionalGroupType {

	NETWORK((short)1, "Red");

	private Short id;
	private String value;

	EInstitutionalGroupType(Short id, String value) {
		this.id = id;
		this.value = value;
	}

	public Short getId() { return id; }
	public String getValue() {
		return value;
	}

	public static EInstitutionalGroupType map(String value) {
		for (EInstitutionalGroupType e : values()) {
			if (e.value.equals(value)) return e;
		}
		throw new NotFoundException("", String.format("El tipo de grupo %s no existe", value));
	}

	public static EInstitutionalGroupType map(Short id) {
		for (EInstitutionalGroupType e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("", String.format("El tipo de grupo con id %s no existe", id));
	}

}
