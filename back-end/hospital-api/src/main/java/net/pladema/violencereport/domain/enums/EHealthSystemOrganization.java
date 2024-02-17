package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EHealthSystemOrganization {

	PROVINCIAL_HOSPITAL(1, "PROVINCIAL_HOSPITAL"),
	SANITARY_REGION(2, "SANITARY_REGION"),
	UPA(3, "UPA"),
	CPA(4, "CPA"),
	POSTAS(5, "POSTAS"),
	SIES(6, "SIES"),
	VACCINATION_CENTER(7, "VACCINATION_CENTER"),
	CETEC(8, "CETEC"),
	MINISTRY_CENTER(9, "MINISTRY_CENTER"),
	CAPS(10, "CAPS"),
	OTHERS(11, "OTHERS");

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
