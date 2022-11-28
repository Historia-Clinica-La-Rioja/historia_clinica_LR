package net.pladema.medicalconsultation.diary.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.controller.service.domain.DepartmentBo;
import net.pladema.establishment.service.domain.ClinicalSpecialtyBo;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryAvailableProtectedAppointmentsBo {

	private Integer diaryId;

	private Integer openingHoursId;

	private boolean overturnMode;

	private LocalDate date;

	private LocalTime hour;

	private InstitutionBasicInfoBo institution;

	private DepartmentBo department;

	private String professionalFullName;

	private ClinicalSpecialtyBo clinicalSpecialty;

	private String doctorOffice;

	private boolean isJointDiary;

}
