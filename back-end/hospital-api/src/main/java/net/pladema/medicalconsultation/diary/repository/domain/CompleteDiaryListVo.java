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
	
	private final Integer healthcareProfessionalId;

	private final String specialtyName;

	public CompleteDiaryListVo(Diary diary, String doctorsOfficeDescription, Integer sectorId,
							   Integer healthcareProfessionalId, String specialtyName) {
		super(diary, doctorsOfficeDescription);
		this.sectorId = sectorId;
		this.healthcareProfessionalId = healthcareProfessionalId;
		this.specialtyName = specialtyName;
	}
	
	

}
