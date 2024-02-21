package net.pladema.medicalconsultation.unsitisfiedDemand.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UnsatisfiedAppointmentDemandBo {

	private List<Short> daysOfWeek;

	@Nullable
	private String aliasOrSpecialtyName;

	private LocalTime initialSearchTime;

	private LocalTime endSearchTime;

	private LocalDate initialSearchDate;

	private LocalDate endingSearchDate;

	private Short modalityId;

	@Nullable
	private Integer practiceId;

	private Integer institutionId;

}
