package net.pladema.patient.controller.dto;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EMedicalCoverageType {
    PREPAGA((short)1,"Prepaga"),
    OBRASOCIAL((short)2,"Obra social"),
	ART((short)3, "ART");

    private Short id;
    private String value;

    EMedicalCoverageType(Short id,String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EMedicalCoverageType map(String value) {
        for (EMedicalCoverageType e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new NotFoundException("medical-coverage-type-not-exists", String.format("El tipo cobertura médica %s no existe", value));
    }

    public static EMedicalCoverageType map(Short id) {
        for (EMedicalCoverageType e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("medical-coverage-type-not-exists", String.format("El tipo cobertura médica con id %s no existe", id));
    }

}
