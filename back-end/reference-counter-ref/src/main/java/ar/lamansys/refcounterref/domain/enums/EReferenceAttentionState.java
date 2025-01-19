package ar.lamansys.refcounterref.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum EReferenceAttentionState {

	PENDING(-1, "Pendiente"),
	ASSIGNED(1, "Asignado"),
	SERVED(2, "Atendido"),
	ABSENT(3, "Ausente");


	private Short id;

	private String description;

	EReferenceAttentionState(Number id, String description) {
		this.id = id.shortValue();
		this.description = description;
	}

	public static EReferenceAttentionState map(Short id) {
		for(EReferenceAttentionState e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("reference-attention_state-not-exists", String.format("El estado de atención de referencia con id %s no existe", id));
	}

	@JsonCreator
	public static EReferenceAttentionState getById(Short id){
		if (id == null)
			return null;
		for(EReferenceAttentionState ras: values()) {
			if(ras.id.equals(id)) return ras;
		}
		throw new NotFoundException("reference-attention_state-not-exists", String.format("El valor %s es inválido", id));
	}

}
