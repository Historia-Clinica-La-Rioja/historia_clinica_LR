package net.pladema.medicalconsultation.appointment.service.domain;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.DocumentAppointmentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DocumentAppointmentBo {

	private Long documentId;

	private Integer appointmentId;

	public static DocumentAppointmentBo makeTo(DocumentAppointmentDto documentAppointmentDto){
		return new DocumentAppointmentBo(
				documentAppointmentDto.getDocumentId(),
				documentAppointmentDto.getAppointmentId()
		);
	}
}
