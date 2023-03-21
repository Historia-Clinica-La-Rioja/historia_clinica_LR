package net.pladema.establishment.service.domain;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ESectorType {

	AMBULATORIO (1,"Ambulatorio"),
	INTERNACION (2,"Internación"),
	GUARDIA(3, "Guardia"),
	DIAGNOSTICO_POR_IMAGEN(4,"Diagnóstico por imagen"),
	HOSPITAL_DE_DIA(5, "Hospital de día"),
	SIN_TIPO(6,"Sin tipo"),
	LABORATORIO(7,"Laboratorio");
	private Short id;
	private String description;
	ESectorType(Number id, String description){
		this.id = id.shortValue();
		this.description = description;
	}

	public static List<ESectorType> getAll(){
		return Stream.of(ESectorType.values()).collect(Collectors.toList());
	}
}
