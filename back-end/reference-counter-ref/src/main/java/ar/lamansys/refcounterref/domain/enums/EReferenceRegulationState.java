package ar.lamansys.refcounterref.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum EReferenceRegulationState {

	WAITING_AUDIT(0, "Esperando auditoría"),
	DONT_REQUIRES_AUDIT (1, "No requiere auditoria"),
	REJECTED (2, "Rechazada"),
	SUGGESTED_REVISION (3, "Revisión sugerida"),
	AUDITED(4, "Auditada");

	private Short id;
	private String description;

	EReferenceRegulationState(Number id, String description){
		this.id = id.shortValue();
		this.description = description;
	}

	@JsonCreator
	public static EReferenceRegulationState getById(Short id){
		if (id == null)
			return null;
		for(EReferenceRegulationState e: values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("referenceRegulationStatus-not-exists", String.format("El valor %s es inválido", id));
	}

	@JsonCreator
	public static List<EReferenceRegulationState> getAll(){
		return Stream.of(EReferenceRegulationState.values()).collect(Collectors.toList());
	}

}
