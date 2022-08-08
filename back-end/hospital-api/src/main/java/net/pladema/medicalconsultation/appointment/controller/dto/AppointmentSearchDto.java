package net.pladema.medicalconsultation.appointment.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AppointmentSearchDto {

	private List<Short> daysOfWeek;
	private String aliasOrSpecialtyName;
	private TimeDto initialSearchTime;
	private TimeDto endSearchTime;
	private DateDto initialSearchDate;
	private DateDto endingSearchDate;

}
