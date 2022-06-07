package net.pladema.staff.service.domain;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum ELicenseNumberTypeBo {

	NATIONAL(1, "Nacional"),
	PROVINCE(2, "Provincial"),
	;

    private Short id;
    private String value;
    ELicenseNumberTypeBo(Number id, String value) {
        this.id = id.shortValue();
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
    public Short getId() {
        return id;
    }

	public static ELicenseNumberTypeBo map(Short id) {
        for(ELicenseNumberTypeBo e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("license-number-type-not-exists", String.format("El tipo de licencia %s no existe", id));
    }
}
