package net.pladema.medicalconsultation.diary.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DiaryOpeningHoursFreeTimesDto {

	private Integer openingHoursId;

	private List<TimeDto> freeTimes;

}
