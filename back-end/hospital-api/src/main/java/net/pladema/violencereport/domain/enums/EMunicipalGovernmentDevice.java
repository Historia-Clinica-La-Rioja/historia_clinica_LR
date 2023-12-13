package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EMunicipalGovernmentDevice {

	GENDER_DIVERSITY(1, "GENDER_DIVERSITY"),
	LOCAL_COMMITTEE_AGAINST_VIOLENCE(2, "LOCAL_COMMITTEE_AGAINST_VIOLENCE"),
	PROTECTION_CHILDREN_TEENS(3, "PROTECTION_CHILDREN_TEENS"),
	DIRECTORATE_CHILDHOOD(4, "PROTECTION_CHILDREN_TEENS"),
	SOCIAL_DEVELOPMENT_AREA(5, "SOCIAL_DEVELOPMENT_AREA"),
	PREVENTION_TREATMENT(6, "PREVENTION_TREATMENT"),
	EDUCATIONAL_INSTITUTION(7, "EDUCATIONAL_INSTITUTION"),;

	private Short id;

	private String value;

	EMunicipalGovernmentDevice(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EMunicipalGovernmentDevice map(Short id) {
		for (EMunicipalGovernmentDevice e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("municipal-state-option-not-exists", String.format("La opción %s no existe", id));
	}

}
