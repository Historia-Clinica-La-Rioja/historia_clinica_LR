package net.pladema.medicalconsultation.virtualConsultation.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EVirtualConsultationPriority {

	LOW(1, "Baja"),
	MEDIUM(2, "Media"),
	HIGH(3, "Alta");

	private Short id;
	private String value;

	EVirtualConsultationPriority(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EVirtualConsultationPriority map(Short id) {
		for (EVirtualConsultationPriority e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("virtual-consultation-priority-not-exists", String.format("La prioridad %s no existe", id));
	}

}
