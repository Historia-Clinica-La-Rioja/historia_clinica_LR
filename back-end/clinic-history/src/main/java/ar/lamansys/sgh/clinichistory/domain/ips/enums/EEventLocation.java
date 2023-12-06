package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum EEventLocation {

	DOMICILIO_PARTICULAR(1, "Domicilio particular"),
	VIA_PUBLICA(2, "Vía pública"),
	LUGAR_DE_TRABAJO(3, "Lugar de trabajo"),
	OTRO(4, "Otro");

	private final Short id;
	private final String value;

	EEventLocation(Number id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}
	@JsonCreator
	public static List<EEventLocation> getAll(){
		return Stream.of(EEventLocation.values()).collect(Collectors.toList());
	}

	public static EEventLocation map(Short id) {
		for(EEventLocation e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("location-not-exists", String.format("El lugar %s no existe", id));
	}

}
