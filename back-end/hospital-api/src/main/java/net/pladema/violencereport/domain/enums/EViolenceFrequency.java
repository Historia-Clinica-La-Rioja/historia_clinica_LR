package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EViolenceFrequency {

	FIRST_TIME(1, "MOTHER"),
	SOMETIMES(2, "SOMETIMES"),
	FREQUENT(3, "FREQUENT"),
	NO_INFORMATION(4, "NO_INFORMATION");

	private Short id;

	private String value;

	EViolenceFrequency(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EViolenceFrequency map(Short id) {
		for (EViolenceFrequency e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("frequency-not-exists", String.format("La frencuencia %s no existe", id));
	}


}