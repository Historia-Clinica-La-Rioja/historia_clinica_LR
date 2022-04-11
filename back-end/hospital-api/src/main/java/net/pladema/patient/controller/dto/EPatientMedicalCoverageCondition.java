package net.pladema.patient.controller.dto;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EPatientMedicalCoverageCondition {

	VOLUNTARIA(1, "Voluntaria"),
	OBLIGATORIA(2, "Obligatoria");

	private Short id;
	private String value;

	EPatientMedicalCoverageCondition(Number id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Short getId() {
		return id;
	}

	public static EPatientMedicalCoverageCondition map(Short id) {
		for (EPatientMedicalCoverageCondition e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("patientMedicalCoverageCondition-not-exists", String.format("El tipo de condicion %s no existe", id));
	}
}