package ar.lamansys.sgh.publicapi.domain;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EExternalEncounterType {
    INTERNACION("INTERNACION"),
    CONSULTA_AMBULATORIA("CONSULTA_AMBULATORIA");

    private String value;

    EExternalEncounterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EExternalEncounterType map(String value) {
        for (EExternalEncounterType e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new NotFoundException("external-encounter-type-not-exists", String.format("El tipo de encuentro %s no existe", value));
    }

}
