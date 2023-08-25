package net.pladema.medicalconsultation.appointment.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EAppointmentModality {

	ON_SITE_ATTENTION(1, "PRESENCIAL"),
	PATIENT_VIRTUAL_ATTENTION(2, "TELECONSULTA DE PACIENTES"),
	SECOND_OPINION_VIRTUAL_ATTENTION(3, "TELECONSULTA DE SEGUNDA OPINION");

	private Short id;
	private String value;

	EAppointmentModality(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EAppointmentModality map(Short id) {
		for (EAppointmentModality e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("appointment-modality-not-exists", String.format("La modalidad %s no existe", id));
	}

}
