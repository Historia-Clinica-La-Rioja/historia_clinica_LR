package net.pladema.sisa.refeps.services.domain;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
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

	public static ELicenseNumberType map(Short id) {
		for (ELicenseNumberType e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("type-not-exists", String.format("El tipo %s no existe", id));
	}

}
