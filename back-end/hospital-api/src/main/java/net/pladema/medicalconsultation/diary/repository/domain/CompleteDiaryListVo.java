package net.pladema.medicalconsultation.diary.repository.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class CompleteDiaryListVo extends DiaryListVo {

	private final Integer sectorId;

	private final Integer clinicalSpecialtyId;
	
	private final Integer healthcareProfessionalId;

	public CompleteDiaryListVo(Integer id, Integer doctorsOfficeId, String doctorsOfficeDescription, LocalDate startDate, LocalDate endDate,
			Short appointmentDuration, Boolean professionalAssignShift, Boolean includeHoliday, Integer sectorId,
			Integer clinicalSpecialtyId, Integer healthcareProfessionalId) {
		super(id, doctorsOfficeId, doctorsOfficeDescription, startDate, endDate, appointmentDuration, professionalAssignShift, includeHoliday);
		this.sectorId = sectorId;
		this.clinicalSpecialtyId = clinicalSpecialtyId;
		this.healthcareProfessionalId = healthcareProfessionalId;
	}
	
	

}
