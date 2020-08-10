package net.pladema.medicalconsultation.diary.repository.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;

@ToString
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class CompleteDiaryListVo extends DiaryListVo {

	private final Integer sectorId;

	private final Integer clinicalSpecialtyId;
	
	private final Integer healthcareProfessionalId;

	public CompleteDiaryListVo(Diary diary, String doctorsOfficeDescription, Integer sectorId,
							   Integer clinicalSpecialtyId, Integer healthcareProfessionalId) {
		super(diary, doctorsOfficeDescription);
		this.sectorId = sectorId;
		this.clinicalSpecialtyId = clinicalSpecialtyId;
		this.healthcareProfessionalId = healthcareProfessionalId;
	}
	
	

}
