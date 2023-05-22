package net.pladema.sisa.refeps.services.domain;

import lombok.Getter;

@Getter
public enum ELicenseNumberType {

	NATIONAL(1, "Nacional", "MN"),
	PROVINCE(2, "Provincial", "MP"),
	;

	private final Short id;
	private final String value;
	private final String acronym;

	ELicenseNumberType(Number id, String value, String acronym) {
		this.id = id.shortValue();
		this.value = value;
		this.acronym = acronym;
	}

}
