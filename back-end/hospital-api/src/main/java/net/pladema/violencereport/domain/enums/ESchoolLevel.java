package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ESchoolLevel {

	NURSERY_SCHOOL(1, "NURSERY_SCHOOL"),
	KINDERGARTEN(2, "KINDERGARTEN"),
	ELEMENTARY_SCHOOL(3, "ELEMENTARY_SCHOOL"),
	HIGH_SCHOOL(4, "HIGH_SCHOOL");

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
