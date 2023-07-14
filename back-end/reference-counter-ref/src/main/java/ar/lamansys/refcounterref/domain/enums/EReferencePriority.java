package ar.lamansys.refcounterref.domain.enums;

import lombok.Getter;

@Getter
public enum EReferencePriority {

	HIGH(1, "Alta prioridad"),

	MEDIUM(2, "Media prioridad"),

	LOW(3, "Baja prioridad");

	private final Short id;
	private final String description;

	EReferencePriority(Number id, String description) {
		this.id = id.shortValue();
		this.description = description;
	}

}
