package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PublicAppointmentStatus implements Serializable {

	@ToString.Include
	private final Short id;

	@ToString.Include
	private final String description;

    public PublicAppointmentStatus(Short id, String description) {
        this.id = id;
        this.description = description;
    }
}
