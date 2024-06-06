package net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.input.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UnsatisfiedAppointmentDemandDto {

	private List<Short> daysOfWeek;

	private String aliasOrSpecialtyName;

	private TimeDto initialSearchTime;

	private TimeDto endSearchTime;

	private DateDto initialSearchDate;

	private DateDto endingSearchDate;

	private EAppointmentModality modality;

	private Integer practiceId;

}
