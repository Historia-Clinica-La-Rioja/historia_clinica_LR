package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum EAnthropometricGraphic {

	LENGTH_HEIGHT_FOR_AGE((short)1),
	WEIGHT_FOR_AGE((short)2),
	BMI_FOR_AGE((short)3),
	HEAD_CIRCUMFERENCE((short)4),
	WEIGHT_FOR_LENGTH((short)5),
	WEIGHT_FOR_HEIGHT((short)6);

	private Short id;

	EAnthropometricGraphic(Number id){
		this.id = id.shortValue();
	}

	@JsonCreator
	public static EAnthropometricGraphic map(Short id) {
		if (id == null)
			return null;
		for(EAnthropometricGraphic e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("chart-not-exist", String.format("El gráfico  %s no existe", id));
	}

	@JsonCreator
	public static List<EAnthropometricGraphic> getAll(){
		return Stream.of(EAnthropometricGraphic.values()).collect(Collectors.toList());
	}

}
