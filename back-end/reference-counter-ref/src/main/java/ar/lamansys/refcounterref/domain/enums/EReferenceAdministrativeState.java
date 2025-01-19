package ar.lamansys.refcounterref.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EReferenceAdministrativeState {

	WAITING_APPROVAL((short)0),
	APPROVED((short)1),
	SUGGESTED_REVISION((short)2	);

	private Short id;

	EReferenceAdministrativeState(Number id){
		this.id = id.shortValue();
	}

	public Short getId(){
		return this.id;
	}

	@JsonCreator
	public static EReferenceAdministrativeState map(Short id){
		if (id == null)
			return null;
		for(EReferenceAdministrativeState e: values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("referenceAdministrativeState-not-exists", String.format("El valor %s es inválido", id));
	}

}
