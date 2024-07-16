package net.pladema.medicalconsultation.appointment.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

import javax.annotation.Nullable;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AppointmentSearchDto {

	private List<Short> daysOfWeek;
	private Integer clinicalSpecialtyId;
	@Nullable
	private Integer diaryId;
	private TimeDto initialSearchTime;
	private TimeDto endSearchTime;
	private DateDto initialSearchDate;
	private DateDto endingSearchDate;
	private EAppointmentModality modality;
	@Nullable
	private Integer practiceId;

}
