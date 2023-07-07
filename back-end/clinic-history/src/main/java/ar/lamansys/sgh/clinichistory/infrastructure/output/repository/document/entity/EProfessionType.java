package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EProfessionType {

	SURGEON((short)1),
	SURGEON_ASSISTANT((short)2),
	ANESTHESIOLOGIST((short)3),
	CARDIOLOGIST((short)4),
	SURGICAL_INSTRUMENT_TECHNICIAN((short)5),
	OBSTETRICIAN((short)6),
	PEDIATRICIAN((short)7),
	PATHOLOGIST((short)8),
	TRANSFUSIONIST((short)9);

	private Short id;

	EProfessionType(Short id){
		this.id = id;
	}

	public static EProfessionType map(Short id) {
		for(EProfessionType e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("profession-type-not-exists", String.format("El tipo de profesion %s no existe", id));
	}

}
