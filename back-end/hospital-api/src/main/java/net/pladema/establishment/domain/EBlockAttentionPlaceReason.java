package net.pladema.establishment.domain;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum EBlockAttentionPlaceReason {

	//See: https://www.hl7.org/fhir/R4/v2/0116/index.html
	HOUSEKEEPING((short) 1, "Mantenimiento"),
	CONTAMINATED((short) 2, "Alerta infectológica"),
	OTHER((short) 3, "Otro"),
	;

	private final Short id;
	private final String description;

	EBlockAttentionPlaceReason(Short id, String description) {
		this.id = id;
		this.description = description;
	}

	public static EBlockAttentionPlaceReason map(Short id) {
		for (EBlockAttentionPlaceReason e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("block-attention-place-reason-not-exists", String.format("El tipo %s no existe", id));
	}

    public static List<EBlockAttentionPlaceReason> getAll() {
		return Stream.of(EBlockAttentionPlaceReason.values()).collect(Collectors.toList());
    }
}
