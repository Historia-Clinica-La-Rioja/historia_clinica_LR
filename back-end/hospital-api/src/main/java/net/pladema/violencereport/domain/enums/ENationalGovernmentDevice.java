package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ENationalGovernmentDevice {

	WOMEN_GENDER_DIVERSITY_MINISTRY(1, "WOMEN_GENDER_DIVERSITY_MINISTRY"),
	CHILDHOOD_ADOLESCENCE_FAMILY_MINISTRY(2, "CHILDHOOD_ADOLESCENCE_FAMILY_MINISTRY"),
	SOCIAL_DEVELOPMENT_MINISTRY(3, "SOCIAL_DEVELOPMENT_MINISTRY"),
	SEDRONAR(4, "SEDRONAR"),
	ANSES(5, "ANSES"),
	CIVIL_REGISTRY(6, "CIVIL_REGISTRY"),
	EDUCATIONAL_INSTITUTION(7, "EDUCATIONAL_INSTITUTION"),
	SECURITY_FORCES(8, "SECURITY_FORCES"),
	JUSTICE_MINISTRY(9, "JUSTICE_MINISTRY"),;

	private Short id;

	private String value;

	ENationalGovernmentDevice(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ENationalGovernmentDevice map(Short id) {
		for (ENationalGovernmentDevice e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("national-state-option-not-exists", String.format("La opción %s no existe", id));
	}


}
