package ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum EAuditType {

	UNAUDITED("UNAUDITED",1),
	TO_AUDIT("TO_AUDIT",2),
	AUDITED("AUDITED",3);

	private final String description;

	private final Short id;

	EAuditType(String description, Integer id) {
		this.description = description;
		this.id = id.shortValue();
	}

	public Short getId() {
		return id;
	}

	@JsonValue
	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static EAuditType fromValue(String value) {
		for (EAuditType auditType : EAuditType.values()) {
			if (auditType.getDescription().equals(value)) {
				return auditType;
			}
		}
		throw new NotFoundException("auditor-type-not-exists", String.format("El tipo de auditoria '%s' no existe", value));
	}

	@JsonCreator
	public static EAuditType getById(Short id) {
		if (id == null)
			return null;
		return Stream.of(values())
				.filter(eat -> id.equals(eat.getId()))
				.findAny()
				.orElseThrow(() -> new NotFoundException("auditType-not-exists", String.format("El valor %s es inválido", id)));

	}

}
