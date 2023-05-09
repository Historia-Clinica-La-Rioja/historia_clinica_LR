package ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums;

public enum EAuditType {

	UNAUDITED(1),
	TO_AUDIT(2),
	AUDITED(3);

	private final Short id;

	EAuditType(Integer id) {
		this.id = id.shortValue();
	}

	public Short getId() {
		return id;
	}
}
