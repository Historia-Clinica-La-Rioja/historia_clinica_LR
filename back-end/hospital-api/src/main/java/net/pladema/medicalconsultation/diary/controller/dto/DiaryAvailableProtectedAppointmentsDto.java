package net.pladema.medicalconsultation.diary.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.controller.dto.DepartmentDto;
import net.pladema.establishment.controller.dto.InstitutionBasicInfoDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryAvailableProtectedAppointmentsDto {

	private Integer diaryId;

	private DateDto date;

	private TimeDto hour;

	private InstitutionBasicInfoDto institution;

	private DepartmentDto department;

	private String professionalFullName;

	private ClinicalSpecialtyDto clinicalSpecialty;

	private String doctorOffice;

	private boolean isJointDiary;

	private Integer openingHoursId;

	private boolean overturnMode;
}
