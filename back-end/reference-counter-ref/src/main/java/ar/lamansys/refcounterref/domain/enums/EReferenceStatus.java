package ar.lamansys.refcounterref.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EReferenceStatus {

	ACTIVE(1, "Activa"),
	ERROR(2, "Erronea"),
	CANCELLED(3, "Cancelada"),
	MODIFIED(4, "Modificada");

	private final Short id;
	private final String value;

	EReferenceStatus(Number id, String value){
		this.id = id.shortValue();
		this.value = value;
	}

	@JsonCreator
	public static EReferenceStatus map(Short id) {
		if (id == null)
			return null;
		for(EReferenceStatus e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("reference-status-not-exists", String.format("El estado de referencia con id %s no existe", id));
	}

}
