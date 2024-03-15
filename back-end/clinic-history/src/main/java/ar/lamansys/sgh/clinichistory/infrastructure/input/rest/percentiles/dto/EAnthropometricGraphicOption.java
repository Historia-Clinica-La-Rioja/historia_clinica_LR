package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.percentiles.dto;


import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EAnthropometricGraphicOption {

	LENGHT_HEIGHT_FOR_AGE((short)1, "Talla para la edad");

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
		throw new NotFoundException("anthropometric-graphic-not-exist", String.format("El gráfico  %s no existe", id));
	}
	
}
