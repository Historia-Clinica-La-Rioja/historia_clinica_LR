package net.pladema.medicalconsultation.appointment.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class AssignedAppointmentDto {

	private String professionalName;

	private String license;

	private List<String> specialties;

	private DateDto date;

	private TimeDto hour;

	private String office;
}
