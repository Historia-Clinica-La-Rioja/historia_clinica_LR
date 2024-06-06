package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EViolenceTowardsUnderageType {

	DIRECT_VIOLENCE(1, "Si, es violencia directa contra NNyA"),
	INDIRECT_VIOLENCE(2, "Si, es una violencia indirecta contra NNyA"),
	NO_VIOLENCE(3, "No"),
	NO_INFORMATION(4, "Sin información");


	private Short id;

	private String value;

	EViolenceTowardsUnderageType(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EViolenceTowardsUnderageType map(Short id) {
		for (EViolenceTowardsUnderageType e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("violence-towards-underage-not-exists", String.format("El estado %s no existe", id));
	}

}
