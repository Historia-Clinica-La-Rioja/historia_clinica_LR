package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EAggressorRelationship {

	PARTNER(1, "PARTNER"),
	EX_PARTNER(2, "EX_PARTNER"),
	FATHER(3, "FATHER"),
	STEPFATHER(4, "STEPFATHER"),
	MOTHER(5, "MOTHER"),
	STEPMOTHER(6, "STEPMOTHER"),
	SON(7, "SON"),
	DAUGHTER(8, "DAUGHTER"),
	SIBLING(9, "SIBLING"),
	SUPERIOR(10, "SUPERIOR"),
	ACQUAINTANCE(11, "ACQUAINTANCE"),
	NO_RELATIONSHIP(12, "NO_RELATIONSHIP"),
	NO_INFORMATION(13, "NO_INFORMATION"),
	DOES_NOT_ANSWER(14, "DOES_NOT_ANSWER"),;

	private Short id;

	private String value;

	EAggressorRelationship(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EAggressorRelationship map(Short id) {
		for (EAggressorRelationship e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("personal-relationship-not-exists", String.format("La relación %s no existe", id));
	}


}
