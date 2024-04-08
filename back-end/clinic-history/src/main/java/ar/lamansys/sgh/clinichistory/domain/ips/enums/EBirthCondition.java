package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter

public enum EBirthCondition {

	BORN_ALIVE(1, "Nacido vivo"),
	FETAL_DEATH(2, "Defunción Fetal");

	private final Short id;

	private final String value;

	EBirthCondition(Number id, String value){
		this.id = id.shortValue();
		this.value = value;
	}

	public static EBirthCondition map(Short id) {
		for(EBirthCondition e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("birthCondition-not-exists", String.format("La condición al nacer %s no existe", id));
	}

}
