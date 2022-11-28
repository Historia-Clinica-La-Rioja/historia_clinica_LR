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

	private final String sectorDescription;
	
	private final Integer healthcareProfessionalId;

	private final String doctorFirstName;

	private final String doctorLastName;

	public CompleteDiaryListVo(Diary diary, String doctorsOfficeDescription, Integer sectorId,
							   String sectorDescription, Integer healthcareProfessionalId, String specialtyName) {
		super(diary, doctorsOfficeDescription, specialtyName);
		this.sectorId = sectorId;
		this.sectorDescription = sectorDescription;
		this.healthcareProfessionalId = healthcareProfessionalId;
		this.doctorFirstName = null;
		this.doctorLastName = null;
	}

	public CompleteDiaryListVo(Diary diary, String doctorsOfficeDescription, Integer sectorId,
							   Integer healthcareProfessionalId, String specialtyName, String doctorFirstName, String doctorLastName) {
		super(diary, doctorsOfficeDescription, specialtyName);
		this.sectorId = sectorId;
		this.sectorDescription = null;
		this.healthcareProfessionalId = healthcareProfessionalId;
		this.doctorFirstName = doctorFirstName;
		this.doctorLastName = doctorLastName;
	}

}
