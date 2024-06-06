package net.pladema.medicalconsultation.appointment.service.domain;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GroupAppointmentBo {

	private Integer diaryId;
	private DateTimeDto date;
}
