package net.pladema.medicalconsultation.diary.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HolidayDto {

	private DateDto date;
	private String description;

}
