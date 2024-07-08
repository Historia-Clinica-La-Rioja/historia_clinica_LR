package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum EProfessionType implements Serializable {

	SURGEON((short)1, "Cirujano"),
	SURGEON_ASSISTANT((short)2, "Ayudante"),
	ANESTHESIOLOGIST((short)3, "Anestesiólogo"),
	CARDIOLOGIST((short)4, "Cardiólogo"),
	SURGICAL_INSTRUMENT_TECHNICIAN((short)5, "Instrumentador"),
	OBSTETRICIAN((short)6, "Obstetra"),
	PEDIATRICIAN((short)7, "Pediatra"),
	PATHOLOGIST((short)8, "Patólogo"),
	TRANSFUSIONIST((short)9, "Transfusionista"),
	NURSE((short) 11, "Enfermero"),
	MEDICAL_SPECIALIST((short) 12, "Especialista médico"),
	NEONATOLOGIST((short) 13, "Neonatólogo"),
	DENTIST((short) 14, "Odontólogo"),
	HEALTHCARE_PROFESSIONAL((short) 15, "Profesional de la salud"),
	OTHER((short) 10, "Otro");

	private final Short id;

	private final String description;

	EProfessionType(Short id, String description){
		this.id = id;
		this.description = description;
	}

	@JsonCreator
	public static List<EProfessionType> getAll(){
		return Stream.of(EProfessionType.values()).collect(Collectors.toList());
	}

	public static EProfessionType map(Short id) {
		for(EProfessionType e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("profession-type-not-exists", String.format("El tipo de profesion %s no existe", id));
	}

}
