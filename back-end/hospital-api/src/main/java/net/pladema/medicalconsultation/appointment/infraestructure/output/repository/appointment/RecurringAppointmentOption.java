package net.pladema.medicalconsultation.appointment.infraestructure.output.repository.appointment;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum RecurringAppointmentOption {

	CURRENT_APPOINTMENT(1, "Este turno", "appointment"),
	CURRENT_AND_NEXT_APPOINTMENTS(2, "Este y los turnos posteriores", "appointment"),
	ALL_APPOINTMENTS(3, "Todos los turnos", "appointment");

	private Short id;
	private String value;
	private String template;

	RecurringAppointmentOption(Number id, String value, String template) {
		this.id = id.shortValue();
		this.value = value;
		this.template = template;
	}

	public String getValue() {
		return value;
	}
	public Short getId() {
		return id;
	}
	public String getTemplate(){
		return template;
	}

	public static RecurringAppointmentOption map(Short id) {
		for(RecurringAppointmentOption e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("recurring-appointment-option-not-exists", String.format("La opción %s no existe", id));
	}
}
