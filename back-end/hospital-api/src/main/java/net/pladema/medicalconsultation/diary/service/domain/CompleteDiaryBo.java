package net.pladema.medicalconsultation.diary.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
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

	private String doctorMiddleNames;

	private String doctorOtherLastNames;

	private String doctorNameSelfDetermination;

	private List<CareLineBo> careLinesInfo;

	private List<ProfessionalPersonBo> associatedProfessionalsInfo;

	private String hierarchicalUnitAlias;
	
	private List<SnomedBo> practicesInfo;

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
		predecessorProfessionalId = diaryBo.getPredecessorProfessionalId();
		hierarchicalUnitId = diaryBo.getHierarchicalUnitId();
	}

	public boolean hasPractices() {
		return this.practicesInfo != null && !this.practicesInfo.isEmpty();
	}
}
