package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DocumentAppointmentDto {

	private Long documentId;

	private Integer appointmentId;
}
