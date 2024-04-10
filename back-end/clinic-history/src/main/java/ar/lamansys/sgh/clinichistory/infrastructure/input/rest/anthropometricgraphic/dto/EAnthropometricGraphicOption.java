package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto;


import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EAnthropometricGraphicOption {

	LENGHT_HEIGHT_FOR_AGE((short)1, "Talla para la edad"),
	WEIGHT_FOR_AGE((short)2, "Peso para la edad"),

	WEIGHT_FOR_LENGTH((short)3, "Talla para peso (0-2 años)"),
	WEIGHT_FOR_HEIGHT((short)4, "Talla para peso (2-5 años)");

	private Short id;
	private String value;

	EAnthropometricGraphicOption(Number id, String value){
		this.id = id.shortValue();
		this.value = value;
	}

	@JsonCreator
	public static EAnthropometricGraphicOption map(Short id) {
		if (id == null)
			return null;
		for(EAnthropometricGraphicOption e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("anthropometric-graphic-option-not-exist", String.format("El gráfico  %s no existe", id));
	}

	@JsonCreator
	public static List<EAnthropometricGraphicOption> getAll(){
		return Stream.of(EAnthropometricGraphicOption.values()).collect(Collectors.toList());
	}
	
}
