package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EViolenceFrequency {

	FIRST_TIME(1, "Primera vez"),
	SOMETIMES(2, "Alguna vez anterior"),
	FREQUENT(3, "Con frecuencia"),
	NO_INFORMATION(4, "Sin información");

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
