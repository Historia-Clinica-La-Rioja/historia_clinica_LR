package ar.lamansys.refcounterref.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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

	@JsonCreator
	public static List<EReferencePriority> getAll(){
		List <EReferencePriority> result = Stream.of(EReferencePriority.values()).collect(Collectors.toList());
		result.sort(Comparator.comparing(EReferencePriority::getId).reversed());
		return result;
	}

}
