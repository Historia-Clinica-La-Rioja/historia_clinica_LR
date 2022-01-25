package ar.lamansys.sgh.publicapi.domain;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EMedicalCoverageType {
    OBRASOCIAL("OBRASOCIAL"),
    PREPAGA("PREPAGA");

    private String value;

    EMedicalCoverageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EMedicalCoverageType map(String value) {
        for (EMedicalCoverageType e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new NotFoundException("external-encounter-type-not-exists", String.format("El tipo de encuentro %s no existe", value));
    }
}
