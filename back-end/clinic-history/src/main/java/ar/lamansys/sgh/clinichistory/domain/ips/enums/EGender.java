package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EGender {

	FEMALE((short) 1, "Femenino"),
	MALE((short) 2, "Masculino"),

	X((short) 3, "Indeterminado");

	private final Short id;

	private final String value;

	EGender (short id, String value) {
		this.id = id;
		this.value = value;
	}

	public static EGender map(Short id) {
		for(EGender e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("gender-not-exists", String.format("El genero %s no existe", id));
	}

}
