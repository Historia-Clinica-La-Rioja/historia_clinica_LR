package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EProvincialGovernmentDevice {

	WOMEN_GENDER_DIVERSITY_MINISTRY(1, "WOMEN_GENDER_DIVERSITY_MINISTRY"),
	PROMOTION_PROTECTION_RIGHTS_ZONAL_SERVICE(2, "PROMOTION_PROTECTION_RIGHTS_ZONAL_SERVICE"),
	COMMUNITY_DEVELOPMENT_MINISTRY(3, "COMMUNITY_DEVELOPMENT_MINISTRY"),
	EDUCATIONAL_INSTITUTION(4, "EDUCATIONAL_INSTITUTION"),
	SECURITY_FORCES(5, "SECURITY_FORCES"),
	JUDICIAL_SYSTEM(6, "JUDICIAL_SYSTEM"),
	PAROLE_BOARD(7, "PAROLE_BOARD"),
	JUVENILE_JUSTICE_INSTITUTION(8, "JUVENILE_JUSTICE_INSTITUTION"),
	JUSTICE_MINISTRY(9, "JUSTICE_MINISTRY"),;

	private Short id;

	private String value;

	EProvincialGovernmentDevice(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EProvincialGovernmentDevice map(Short id) {
		for (EProvincialGovernmentDevice e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("provincial-state-option-not-exists", String.format("La opción %s no existe", id));
	}


}
