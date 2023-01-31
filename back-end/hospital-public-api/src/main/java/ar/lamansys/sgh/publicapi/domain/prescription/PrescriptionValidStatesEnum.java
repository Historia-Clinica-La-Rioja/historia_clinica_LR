package ar.lamansys.sgh.publicapi.domain.prescription;

import java.util.Arrays;
import java.util.List;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum PrescriptionValidStatesEnum {
	ACTIVO((short) 1),
	DISPENSADO_CERRADO((short) 2),
	DISPENSADO_PROVISORIO((short) 3),
	VENCIDO((short) 4),
	CANCELADO((short) 5);

	private final List<Short> ids;
	PrescriptionValidStatesEnum(Short... ids){
		this.ids = Arrays.asList(ids);
	}

	public static PrescriptionValidStatesEnum map(Short id) {
		for(PrescriptionValidStatesEnum e : values()) {
			if(e.ids.contains(id)) return e;
		}
		throw new NotFoundException("status-not-exists", String.format("El tipo de estado de receta %s no existe", id));
	}

	public static boolean isValidTransition(Short e1, Short e2) {

		if(DISPENSADO_CERRADO.equals(map(e1))) {
			return false;
		}

		if(VENCIDO.equals(map(e1)) || VENCIDO.equals(map(e2))) {
			return false;
		}

		if(ACTIVO.equals(map(e1)) && CANCELADO.equals(map(e2))) {
			return false;
		}

		return !e1.equals(e2);
	}
}
