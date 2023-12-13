package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ERelationshipLength {

	UP_TO_SIX_MONTHS(1, "UP_TO_SIX_MONTHS"),
	ONE_YEAR(2, "ONE_YEAR"),
	MORE_THAN_ONE_YEAR(3, "MORE_THAN_ONE_YEAR");

	private Short id;

	private String value;

	ERelationshipLength(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ERelationshipLength map(Short id) {
		for (ERelationshipLength e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("relationship-length-not-exists", String.format("La duración %s no existe", id));
	}


}
