package net.pladema.medicalconsultation.appointment.infrastructure.output.repository.appointment;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.medicalconsultation.appointment.service.domain.RecurringTypeBo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RecurringAppointmentType {

	NO_REPEAT(1, "No se repite", "appointment"),
	EVERY_WEEK(2, "Cada semana", "appointment"),
	CUSTOM(3, "Personalizar", "appointment");

	private Short id;
	private String value;
	private String template;

	RecurringAppointmentType(Number id, String value, String template) {
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

	public static RecurringAppointmentType map(Short id) {
		for(RecurringAppointmentType e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("recurring-appointment-type-not-exists", String.format("El tipo de turno recurrente %s no existe", id));
	}

	public static List<RecurringTypeBo> getAllValues() {
		List<RecurringTypeBo> result = Arrays.stream(values())
				.map(v -> new RecurringTypeBo(v.id, v.value))
				.collect(Collectors.toList());
		return result;
	}
}
