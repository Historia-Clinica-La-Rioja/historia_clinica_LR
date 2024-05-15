package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EProfessionType {

	SURGEON((short)1, "Cirujano"),
	SURGEON_ASSISTANT((short)2, "Ayudante"),
	ANESTHESIOLOGIST((short)3, "Anestesiólogo"),
	CARDIOLOGIST((short)4, "Cardiologo"),
	SURGICAL_INSTRUMENT_TECHNICIAN((short)5, "Instrumentadora"),
	OBSTETRICIAN((short)6, "Obstetra"),
	PEDIATRICIAN((short)7, "Pediatra"),
	PATHOLOGIST((short)8, "Patologo"),
	TRANSFUSIONIST((short)9, "Transfusionista"),
	OTHER((short) 10, "Otro");

	private Short id;

	private String description;

	EProfessionType(Short id, String description){
		this.id = id;
		this.description = description;
	}

	public static EProfessionType map(Short id) {
		for(EProfessionType e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("profession-type-not-exists", String.format("El tipo de profesion %s no existe", id));
	}

}
