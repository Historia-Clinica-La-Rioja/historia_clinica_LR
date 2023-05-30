package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class StudyAppointmentDto {

	private Integer patientId;
	private String patientFullName;
	private Short statusId;
	private DateTimeDto actionTime;
	private InformerObservationDto informerObservations;

}
