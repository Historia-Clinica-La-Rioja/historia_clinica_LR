package ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum EPatientType {

	PERMANENT(1),
	VALIDATED(2),
	TEMPORARY(3),
	HISTORIC(4),
	TELEPHONIC(5),
	REJECTED(6),
	NOT_VALIDATED_PERMANENT(7),
	EMERGENCY_CARE_TEMPORARY(8);

	private final Short id;

	EPatientType(Integer id) {
		this.id = id.shortValue();
	}

	public Short getId() {
		return id;
	}

	public static List<Short> getAllTypeIdsForAudit() {
		return Arrays.asList(
				PERMANENT.id,
				VALIDATED.id,
				TEMPORARY.id,
				NOT_VALIDATED_PERMANENT.id,
				REJECTED.id
		);
	}

	public static EPatientType map(Short id) {
		if (id == null) {
			return null;
		}
		for (EPatientType e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("patient-type-not-exists", String.format("El tipo de paciente %s no es válido", id));
	}

}
