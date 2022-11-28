package net.pladema.medicalconsultation.appointment.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentShortSummaryDto {

	private String institution;

	private DateDto date;

	private TimeDto hour;

	private String doctorFullName;

}
