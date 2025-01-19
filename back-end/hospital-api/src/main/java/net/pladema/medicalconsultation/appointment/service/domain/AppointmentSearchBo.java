package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class AppointmentSearchBo {

	private List<Short> daysOfWeek;
	private Integer clinicalSpecialtyId;
	private Integer diaryId;
	private String aliasOrSpecialtyName;
	private LocalTime initialSearchTime;
	private LocalTime endSearchTime;
	private LocalDate initialSearchDate;
	private LocalDate endingSearchDate;
	private EAppointmentModality modality;
	private Integer practiceId;

	public AppointmentSearchBo(List<Short> daysOfWeek, String aliasOrSpecialtyName, LocalTime initialSearchTime, LocalTime endSearchTime, LocalDate initialSearchDate,
							   LocalDate endingSearchDate, Integer practiceSnomedId) {
		this.daysOfWeek = daysOfWeek;
		this.aliasOrSpecialtyName = aliasOrSpecialtyName;
		this.initialSearchTime = initialSearchTime;
		this.endSearchTime = endSearchTime;
		this.initialSearchDate = initialSearchDate;
		this.endingSearchDate = endingSearchDate;
		this.practiceId = practiceSnomedId;
	}

}
