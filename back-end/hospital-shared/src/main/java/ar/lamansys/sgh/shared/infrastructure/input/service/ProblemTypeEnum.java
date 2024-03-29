package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ProblemTypeEnum {
	DIAGNOSIS("439401001"),
	PROBLEM("55607006"),
	PRIOR("57177007"),
	CHRONIC("-55607006"),
	OTHER("00000001"),
	POSTOPERATIVE_DIAGNOSIS("406521002"),
	PREOPERATIVE_DIAGNOSIS("406520001");

	private final String id;

	ProblemTypeEnum(String i) {
		id = i;
	}

	public static ProblemTypeEnum map(String id) {
		for(ProblemTypeEnum e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("problem-not-exists", String.format("El tipo de problema %s no existe", id));
	}
}
