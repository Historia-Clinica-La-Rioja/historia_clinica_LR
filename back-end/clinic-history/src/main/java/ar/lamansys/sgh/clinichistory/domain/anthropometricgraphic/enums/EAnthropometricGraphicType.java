package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EAnthropometricGraphicType {

	PERCENTILES((short)1, "Gráfico en percentilos"),
	ZSCORE((short)2, "Puntaje Z"),
	EVOLUTION((short)3, "Evolución");

	private Short id;
	private String value;

	EAnthropometricGraphicType(Number id, String value){
		this.id = id.shortValue();
		this.value = value;
	}

	@JsonCreator
	public static EAnthropometricGraphicType map(Short id) {
		if (id == null)
			return null;
		for(EAnthropometricGraphicType e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("chart-type-not-exist", String.format("El tipo de gráfico  %s no existe", id));
	}

	@JsonCreator
	public static List<EAnthropometricGraphicType> getAll(){
		return Stream.of(EAnthropometricGraphicType.values()).collect(Collectors.toList());
	}

}
