package net.pladema.emergencycare.repository;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EmergencyEpisodeAttendSectorType {

	CONSULTORIO(1, "Consultorio"),
	SHOCKROOM(2, "Shockroom"),
	HABITACION(3, "Habitación");

	private final Short id;
	private final String description;

	EmergencyEpisodeAttendSectorType(Number id, String description) {
		this.id = id.shortValue();
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	public Short getId() {
		return id;
	}

	public static EmergencyEpisodeAttendSectorType map(Short id) {
		for(EmergencyEpisodeAttendSectorType e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("emergency-episode-attend-sector-not-exists", String.format("El tipo de sector para atender %s no existe", id));
	}

	@JsonCreator
	public static List<EmergencyEpisodeAttendSectorType> getAll(){
		return Stream.of(EmergencyEpisodeAttendSectorType.values()).collect(Collectors.toList());
	}
}
