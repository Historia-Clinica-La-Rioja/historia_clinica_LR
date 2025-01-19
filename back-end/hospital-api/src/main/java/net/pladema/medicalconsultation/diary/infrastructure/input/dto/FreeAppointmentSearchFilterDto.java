package net.pladema.medicalconsultation.diary.infrastructure.input.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

@Getter
@Setter
@NoArgsConstructor
public class FreeAppointmentSearchFilterDto {

	private EAppointmentModality modality;

	private boolean mustBeProtected;

	private DateDto date;

}
