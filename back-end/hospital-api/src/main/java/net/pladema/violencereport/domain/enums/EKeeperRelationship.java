package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EKeeperRelationship {

	MOTHER(1, "Madre"),
	FATHER(2, "Padre"),
	GRANDPARENT(3, "Abuelo/a"),
	UNCLE_OR_AUNT(4, "Tío/a"),
	BROTHER_OR_SISTER(5, "Hermano/a"),
	RELATED(6, "Referente"),
	OTHER(7, "Otro");

	private Short id;

	private String value;

	EKeeperRelationship(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EKeeperRelationship map(Short id) {
		for (EKeeperRelationship e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("interpersonal-relationship-not-exists", String.format("La relación %s no existe", id));
	}

}
