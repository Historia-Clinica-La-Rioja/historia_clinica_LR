package ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class SingleAttentionInfoDto {
	private Long id;
	private Long encounterId;
	private DateDto attentionDate;
	private ClinicalSpecialityDto speciality;
	private PersonInfoDto patient;
	private CoverageActivityInfoDto coverage;
	private String scope;
	private InternmentDto internmentInfo;
	private ProfessionalDto responsibleDoctor;
	private SingleDiagnosticDto singleDiagnosticDto;
	private DateTimeDto attentionDateWithTime;
	private PersonExtendedInfoDto personExtendedInfo;
	private DateTimeDto emergencyCareAdministrativeDischargeDateTime;
	private Boolean billable;
}
