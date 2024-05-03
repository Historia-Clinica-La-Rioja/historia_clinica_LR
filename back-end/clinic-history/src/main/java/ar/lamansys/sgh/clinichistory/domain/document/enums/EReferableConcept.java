package ar.lamansys.sgh.clinichistory.domain.document.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EReferableConcept {

	ALLERGY(1),
	PERSONAL_HISTORY(2);

	private final Short id;

	EReferableConcept(Integer id) {
		this.id = id.shortValue();
	}

	public static EReferableConcept map(Short id) {
		for (EReferableConcept e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("concept-not-exists", String.format("El concepto %s no existe", id));
	}

}
