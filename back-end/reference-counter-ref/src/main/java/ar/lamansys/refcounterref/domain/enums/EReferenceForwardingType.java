package ar.lamansys.refcounterref.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum EReferenceForwardingType {

	REGIONAL(1, "Regional"),
	DOMAIN (2, "Dominio");

	private Short id;

	private String description;

	EReferenceForwardingType(Number id, String description){
		this.id = id.shortValue();
		this.description = description;
	}

	@JsonCreator
	public static EReferenceForwardingType getById(Short id){
		if (id == null)
			return null;
		for(EReferenceForwardingType eft: values()) {
			if(eft.id.equals(id)) return eft;
		}
		throw new NotFoundException("reference-forwarding_type-not-exists", String.format("El valor %s es inválido", id));
	}

}
