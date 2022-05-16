package net.pladema.medicalconsultation.appointment.service.fetchappointments.domain;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class AppointmentStatusBo  {

	@ToString.Include
	private final Short id;

	@ToString.Include
	private final String description;

    public AppointmentStatusBo(Short id, String description) {
        this.id = id;
        this.description = description;
    }
}
