package net.pladema.staff.service.domain;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ELicenseNumberTypeBo {

	NATIONAL(1, "Nacional", "MN"),
	PROVINCE(2, "Provincial", "MP"),
	;

    private final Short id;
    private final String value;
	private final String acronym;
    ELicenseNumberTypeBo(Number id, String value, String acronym) {
        this.id = id.shortValue();
        this.value = value;
		this.acronym = acronym;
    }
	public static ELicenseNumberTypeBo map(Short id) {
        for(ELicenseNumberTypeBo e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("license-number-type-not-exists", String.format("El tipo de licencia %s no existe", id));
    }
}
