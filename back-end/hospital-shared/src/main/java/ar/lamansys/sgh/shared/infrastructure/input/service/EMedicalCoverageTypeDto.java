package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EMedicalCoverageTypeDto {
    OBRASOCIAL("OBRASOCIAL"),
    PREPAGA("PREPAGA");

    private String value;

    EMedicalCoverageTypeDto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EMedicalCoverageTypeDto map(String value) {
        for (EMedicalCoverageTypeDto e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new NotFoundException("external-encounter-type-not-exists", String.format("El tipo de encuentro %s no existe", value));
    }
}