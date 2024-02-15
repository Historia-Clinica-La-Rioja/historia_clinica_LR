package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EIntermentIndicationStatus {

	YES(1, "YES"),
	AS_PROTECTIVE_MEASURE(2, "AS_PROTECTIVE_MEASURE"),
	NO(3, "NO");

	private Short id;

	private String value;

	EIntermentIndicationStatus(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EIntermentIndicationStatus map(Short id) {
		for (EIntermentIndicationStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("internment-indication-requirement-not-exists", String.format("El requerimiento %s no existe", id));
	}


}
