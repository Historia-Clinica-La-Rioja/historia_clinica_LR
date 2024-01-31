package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ESchoolLevel {

	NURSERY_SCHOOL(1, "Maternal"),
	KINDERGARTEN(2, "Inicial"),
	ELEMENTARY_SCHOOL(3, "Primario"),
	HIGH_SCHOOL(4, "Secundario");

	private Short id;

	private String value;

	ESchoolLevel(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ESchoolLevel map(Short id) {
		for (ESchoolLevel e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("school-level-not-exists", String.format("El nivel %s no existe", id));
	}

}
