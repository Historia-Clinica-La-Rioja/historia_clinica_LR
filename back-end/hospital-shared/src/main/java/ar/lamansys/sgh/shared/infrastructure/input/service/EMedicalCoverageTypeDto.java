package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EMedicalCoverageTypeDto {
	PREPAGA((short)1,"PREPAGA"),
	OBRASOCIAL((short)2,"OBRASOCIAL"),
	ART((short)3,"ART");

	private Short id;
	private String value;

    EMedicalCoverageTypeDto(Short id, String value) {
        this.id = id;
		this.value = value;
    }

    public String getValue() {
        return value;
    }

	public Short getId(){ return id; }

    public static EMedicalCoverageTypeDto map(String value) {
        for (EMedicalCoverageTypeDto e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new NotFoundException("external-medical-coverage-type-not-exists", String.format("El tipo de cobertura %s no existe", value));
    }

	public static EMedicalCoverageTypeDto map(Short id) {
		for (EMedicalCoverageTypeDto e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("external-medical-coverage-type-not-exists", String.format("El tipo de cobertura con id %s no existe", id));
	}

}