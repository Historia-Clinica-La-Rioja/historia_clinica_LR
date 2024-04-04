package net.pladema.medicalconsultation.appointment.domain.enums;


import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EAppointmentExpiredReason {

	OTHER(1, "Otro");

	private Short id;
	private String value;

	EAppointmentExpiredReason(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public static EAppointmentExpiredReason map(Short id) {
		for (EAppointmentExpiredReason e : values())
			if (e.id.equals(id)) return e;
		throw new NotFoundException("appointment-expired-reason-not-exists", String.format("El motivo de registro de turno vencido con id %s no existe", id));
	}

}
