package net.pladema.patient.service.domain.enums;

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

}
