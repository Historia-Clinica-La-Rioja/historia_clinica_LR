package ar.lamansys.virtualConsultation.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EVirtualConsultationResponsibleProfessionalAvailability {

	AVAILABLE(0, "DISPONIBLE"),
	NOT_AVAILABLE(1, "NO DISPONIBLE");

	private Short id;
	private String value;

	EVirtualConsultationResponsibleProfessionalAvailability(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EVirtualConsultationResponsibleProfessionalAvailability map(Short id) {
		for (EVirtualConsultationResponsibleProfessionalAvailability e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("virtual-consultation-professional-status-not-exists", String.format("El estado %s no existe", id));
	}

	public static EVirtualConsultationResponsibleProfessionalAvailability map(Boolean value) {
		if (value)
			return EVirtualConsultationResponsibleProfessionalAvailability.AVAILABLE;
		return EVirtualConsultationResponsibleProfessionalAvailability.NOT_AVAILABLE;
	}

}
