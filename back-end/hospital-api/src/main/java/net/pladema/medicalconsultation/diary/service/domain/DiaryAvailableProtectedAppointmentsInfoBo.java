package net.pladema.medicalconsultation.diary.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.controller.service.domain.DepartmentBo;
import net.pladema.establishment.service.domain.ClinicalSpecialtyBo;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryAvailableProtectedAppointmentsInfoBo {

	private Integer diaryId;

	private Short protectedAppointmentsPercentage;

	private Short appointmentDuration;

	private LocalDate startDate;

	private LocalDate endDate;

	private List<DiaryOpeningHoursBo> openingHours;

	private Integer openingHoursId;

	private boolean overturnMode;

	private LocalDate date;

	private LocalTime hour;

	private InstitutionBasicInfoBo institution;

	private DepartmentBo department;

	private String professionalFullName;

	private ClinicalSpecialtyBo clinicalSpecialty;

	private String doctorOffice;

	private Boolean isJointDiary;

	public DiaryAvailableProtectedAppointmentsInfoBo(Integer diaryId, Short protectedAppointmentsPercentage,
													 Short appointmentDuration, LocalDate startDate,
													 LocalDate endDate, InstitutionBasicInfoBo institution,
													 DepartmentBo department, String professionalFulName,
													 ClinicalSpecialtyBo clinicalSpecialty, String doctorOffice,
													 Boolean isJointDiary) {
		this.diaryId = diaryId;
		this.institution = institution;
		this.protectedAppointmentsPercentage = protectedAppointmentsPercentage;
		this.appointmentDuration = appointmentDuration;
		this.startDate = startDate;
		this.endDate = endDate;
		this.department = department;
		this.professionalFullName = professionalFulName;
		this.clinicalSpecialty = clinicalSpecialty;
		this.doctorOffice = doctorOffice;
		this.isJointDiary = isJointDiary;
	}
}
