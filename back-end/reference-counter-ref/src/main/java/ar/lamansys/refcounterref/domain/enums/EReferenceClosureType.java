package ar.lamansys.refcounterref.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EReferenceClosureType {

	CONTINUA_OBSERVACION(1, "Continúa en observación"),
	INICIA_TRATAMIENTO(2, "Inicia tratamiento en centro de referencia"),
	REQUIERE_ESTUDIOS(3, "Requiere estudios complementarios"),
	CONTRARREFERENCIA(4, "Contrarreferencia");

	private final Short id;
	private final String description;

	EReferenceClosureType(Number id, String description) {
		this.id = id.shortValue();
		this.description = description;
	}

	@JsonCreator
	public static List<EReferenceClosureType> getAll(){
		return Stream.of(EReferenceClosureType.values()).collect(Collectors.toList());
	}

	@JsonCreator
	public static EReferenceClosureType getById(Short id){
		if (id == null)
			return null;
		for(EReferenceClosureType rcrt: values()) {
			if(rcrt.id.equals(id)) return rcrt;
		}
		throw new NotFoundException("referenceClosureType-not-exists", String.format("El valor %s es inválido", id));
	}

	public Short getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}
