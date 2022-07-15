package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum ENursingRecordStatus {

	COMPLETED(1, "completed"),
	REJECTED(2, "rejected"),
	PENDING(3, "pending");

	private Short id;
	private String value;

	ENursingRecordStatus(Number id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Short getId() {
		return id;
	}

	public static ENursingRecordStatus map(Short id) {
		for (ENursingRecordStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("nursing-record-status-not-exists", String.format("El estado de toma %s no existe", id));
	}

	public static ENursingRecordStatus map(String value) {
		for (ENursingRecordStatus e : values()) {
			if (e.value.equals(value)) return e;
		}
		throw new NotFoundException("nursing-record-status-not-exists", String.format("El estado de toma %s no existe", value));
	}

}
