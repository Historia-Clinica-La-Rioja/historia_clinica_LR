package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EExternalCauseType {

	ACCIDENT("55566008", "Accidente"),
	SELF_INFLICTED_INJURY ("276853009", "Lesión autoinfligida"),
	AGRESSION("61372001", "Agresión"),
	IGNORED ("00000001", "Se ignora");

	private String id;
	private String value;

	EExternalCauseType(String id, String value) {
		this.id = id;
		this.value = value;;
	}

	public String getValue() {
		return value;
	}
	public String getId() {
		return id;
	}

	public static EExternalCauseType map(String id) {
		for(EExternalCauseType e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("cause-not-exists", String.format("La causa %s no existe", id));
	}

}
