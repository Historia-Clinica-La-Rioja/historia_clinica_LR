package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EInternmentPlace {

    FLOOR((short) 1, "Se interna en piso"),
    INTENSIVE_CARE_UNIT((short) 2, "Se interna en Unidad de terapia intensiva");

    private final Short id;
    private final String description;

    EInternmentPlace(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    public static EInternmentPlace map(Short id) {
        for (EInternmentPlace e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("internment-place-not-exists", String.format("El tipo %s no existe", id));
    }
}
