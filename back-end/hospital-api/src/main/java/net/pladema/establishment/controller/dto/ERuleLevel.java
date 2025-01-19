package net.pladema.establishment.controller.dto;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ERuleLevel {

	GENERAL((short)0, "General"),
	LOCAL((short)1, "Local");

	private Short id;
	private String value;

	ERuleLevel(Short id, String value) {
		this.id = id;
		this.value = value;
	}

	public Short getId() { return id; }
	public String getValue() {
		return value;
	}

	public static ERuleLevel map(String value) {
		for (ERuleLevel e : values()) {
			if (e.value.equals(value)) return e;
		}
		throw new NotFoundException("", String.format("El nivel de regla %s no existe", value));
	}

	public static ERuleLevel map(Short id) {
		for (ERuleLevel e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("", String.format("El nivel de regla con id %s no existe", id));
	}

	@JsonCreator
	public static List<ERuleLevel> getAll(){
		return Stream.of(ERuleLevel.values()).collect(Collectors.toList());
	}

}
