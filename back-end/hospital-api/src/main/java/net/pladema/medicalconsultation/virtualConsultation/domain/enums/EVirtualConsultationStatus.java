package net.pladema.medicalconsultation.virtualConsultation.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EVirtualConsultationStatus {

	PENDING(1, "PENDIENTE"),
	IN_PROGRESS(2, "EN CURSO"),
	FINISHED(3, "FINALIZADA"),
	CANCELED(4, "CANCELADA");

	private Short id;
	private String value;

	EVirtualConsultationStatus(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EVirtualConsultationStatus map(Short id) {
		for (EVirtualConsultationStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("virtual-consultation-status-not-exists", String.format("El estado %s no existe", id));
	}

}
