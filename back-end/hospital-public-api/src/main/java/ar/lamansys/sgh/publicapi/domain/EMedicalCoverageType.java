package ar.lamansys.sgh.publicapi.domain;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EMedicalCoverageType {
	PREPAGA((short)1, "PREPAGA"),
    OBRASOCIAL((short)2, "OBRASOCIAL"),
	ART((short)3, "ART");

	private Short id;
    private String value;

    EMedicalCoverageType(Short id, String value) {
        this.id = id;
		this.value = value;
    }

	public Short getId() {
		return id;
	}

    public String getValue() {
        return value;
    }

    public static EMedicalCoverageType map(String value) {
        for (EMedicalCoverageType e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new NotFoundException("medical-coverage-type-not-exists", String.format("El tipo de cobertra %s no existe", value));
    }

	public static EMedicalCoverageType map(Short id) {
		for (EMedicalCoverageType e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("medical-coverage-type-not-exists", String.format("El tipo de cobertra %s no existe", id));
	}
}
