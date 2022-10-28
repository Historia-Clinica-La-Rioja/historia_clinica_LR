package ar.lamansys.sgh.publicapi.domain;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum GenderEnum {
    FEMALE((short) 1),
    MALE((short) 2),

	X((short) 3);

    private final Short id;

    GenderEnum(short i) {
        id = i;
    }

	public static GenderEnum map(Short id) {
		for(GenderEnum e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("genero-not-exists", String.format("El genero %s no existe", id));
	}
}
