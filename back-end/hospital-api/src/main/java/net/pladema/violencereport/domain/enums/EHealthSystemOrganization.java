package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EHealthSystemOrganization {

	PROVINCIAL_HOSPITAL(1, "Hospital provincial"),
	SANITARY_REGION(2, "Sede región sanitaria"),
	UPA(3, "UPA"),
	CPA(4, "CPA"),
	POSTAS(5, "Postas"),
	SIES(6, "SIES"),
	VACCINATION_CENTER(7, "Vacunatorio"),
	CETEC(8, "CETEC"),
	MINISTRY_CENTER(9, "Sede central del ministerio"),
	CAPS(10, "CAPS"),
	OTHERS(11, "Otros");

	private Short id;

	private String value;

	EHealthSystemOrganization(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EHealthSystemOrganization map(Short id) {
		for (EHealthSystemOrganization e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("health-system-organization-not-exists", String.format("La organización %s no existe", id));
	}


}
