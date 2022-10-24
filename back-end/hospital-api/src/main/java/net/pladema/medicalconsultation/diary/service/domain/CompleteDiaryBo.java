package net.pladema.medicalconsultation.diary.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompleteDiaryBo extends DiaryBo {

	private Integer sectorId;

	private String specialtyName;

	public CompleteDiaryBo(DiaryBo diaryBo) {
		appointmentDuration = diaryBo.getAppointmentDuration();
		id = diaryBo.getId();
		doctorsOfficeId = diaryBo.getDoctorsOfficeId();
		startDate = diaryBo.getStartDate();
		endDate = diaryBo.getEndDate();
		automaticRenewal = diaryBo.isAutomaticRenewal();
		professionalAssignShift = diaryBo.isProfessionalAssignShift();
		includeHoliday = diaryBo.isIncludeHoliday();
		alias = diaryBo.getAlias();
	}

}
