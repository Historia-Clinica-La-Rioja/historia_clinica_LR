package net.pladema.medicalconsultation.diary.repository.domain;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class CompleteDiaryListVo extends DiaryListVo {

	private final Integer sectorId;

	private final Integer clinicalSpecialtyId;
	
	private final Integer healthcareProfessionalId;

	public CompleteDiaryListVo(Integer id, Integer doctorsOfficeId, LocalDate startDate, LocalDate endDate,
			Short appointmentDuration, Boolean professionalAssignShift, Boolean includeHoliday, Integer sectorId,
			Integer clinicalSpecialtyId, Integer healthcareProfessionalId) {
		super(id, doctorsOfficeId, startDate, endDate, appointmentDuration, professionalAssignShift, includeHoliday);
		this.sectorId = sectorId;
		this.clinicalSpecialtyId = clinicalSpecialtyId;
		this.healthcareProfessionalId = healthcareProfessionalId;
	}
	
	

}
