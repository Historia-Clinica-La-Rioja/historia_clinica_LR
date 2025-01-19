package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EPregnancyTermination {

	VAGINAL(1, "Vaginal"),
	CESAREAN(2, "Cesárea"),
	UNDEFINED(3, "Sin definir");

	private final Short id;

	private final String value;

	EPregnancyTermination(Number id, String value){
		this.id = id.shortValue();
		this.value = value;
	}

	public static EPregnancyTermination map(Short id) {
		for(EPregnancyTermination e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("pregnancyTermination-not-exists", String.format("El tipo de terminación de embarazo %s no existe", id));
	}

}
