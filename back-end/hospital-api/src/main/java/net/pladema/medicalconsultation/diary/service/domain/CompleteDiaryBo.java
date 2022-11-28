package net.pladema.medicalconsultation.diary.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.service.domain.CareLineBo;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompleteDiaryBo extends DiaryBo {

	private Integer sectorId;

	private String sectorDescription;

	private String doctorFirstName;

	private String doctorLastName;

	private List<CareLineBo> careLinesInfo;

	private List<ProfessionalPersonBo> associatedProfessionalsInfo;

	public CompleteDiaryBo(DiaryBo diaryBo) {
		appointmentDuration = diaryBo.getAppointmentDuration();
		id = diaryBo.getId();
		doctorsOfficeId = diaryBo.getDoctorsOfficeId();
		doctorsOfficeDescription = diaryBo.getDoctorsOfficeDescription();
		clinicalSpecialtyName = diaryBo.getClinicalSpecialtyName();
		startDate = diaryBo.getStartDate();
		endDate = diaryBo.getEndDate();
		automaticRenewal = diaryBo.isAutomaticRenewal();
		professionalAssignShift = diaryBo.isProfessionalAssignShift();
		includeHoliday = diaryBo.isIncludeHoliday();
		alias = diaryBo.getAlias();
		protectedAppointmentsPercentage = diaryBo.getProtectedAppointmentsPercentage();
	}

}
