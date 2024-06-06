package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EAggressorRelationship {

	PARTNER(1, "Pareja/novio/a"),
	EX_PARTNER(2, "Ex pareja"),
	FATHER(3, "Padre"),
	STEPFATHER(4, "Progenitor afín (padrastro)"),
	MOTHER(5, "Madre"),
	STEPMOTHER(6, "Progenitora afín (madrastra)"),
	SON(7, "Hijo"),
	DAUGHTER(8, "Hija"),
	SIBLING(9, "Hermano/a"),
	SUPERIOR(10, "Superior jerárquico"),
	ACQUAINTANCE(11, "Otra/o conocida/o"),
	NO_RELATIONSHIP(12, "Sin vínculo"),
	NO_INFORMATION(13, "Sin información"),
	DOES_NOT_ANSWER(14, "No contesta"),;

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
