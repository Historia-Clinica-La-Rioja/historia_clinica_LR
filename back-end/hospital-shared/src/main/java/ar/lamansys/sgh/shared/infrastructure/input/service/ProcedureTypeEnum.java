package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ProcedureTypeEnum {
	PROCEDURE((short)1, "71388002"),
	SURGICAL_PROCEDURE((short)2, "387713003"),
	ANESTHESIA_PROCEDURE((short)3,"1263452006"),
	CULTURE((short)4),
	FROZEN_SECTION_BIOPSY((short)5),
	DRAINAGE((short)6);

	private final Short id;

	private final String sctidCode;

	ProcedureTypeEnum(Short id, String code) {
		this.id = id;
		this.sctidCode = code;
	}

	ProcedureTypeEnum(Short id) {
		this.id = id;
		this.sctidCode = null;
	}

	public static ProcedureTypeEnum map(Short id) {
		for(ProcedureTypeEnum e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("procedure-not-exists", String.format("El tipo de procedimiento %s no existe", id));
	}
}
