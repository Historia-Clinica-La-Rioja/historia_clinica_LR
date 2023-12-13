package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EViolenceTowardsUnderageType {

	DIRECT_VIOLENCE(1, "DIRECT_VIOLENCE"),
	INDIRECT_VIOLENCE(2, "INDIRECT_VIOLENCE"),
	NO_VIOLENCE(3, "NO_VIOLENCE"),
	NO_INFORMATION(4, "NO_INFORMATION");


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
