package net.pladema.snvs.infrastructure.output.repository.snvs.entity;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EEnvironment {

	TEST(0, "DEV"),
	UAT(1, "PROD"),
	QA(2, "QA"),
	;

	private Short id;
	private String value;

	EEnvironment(Number id, String value) {
		this.id = id.shortValue();
		this.value = value;;
	}

	public String getValue() {
		return value;
	}
	public Short getId() {
		return id;
	}

	public static EEnvironment map(Short id) {
		for(EEnvironment e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("environment-not-exists", String.format("El ambiente %s no existe", id));
	}

	public static EEnvironment map(String value) {
		for(EEnvironment e : values()) {
			if(e.value.equals(value)) return e;
		}
		throw new NotFoundException("environment-not-exists", String.format("El ambiente %s no existe", value));
	}
}
