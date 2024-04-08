package net.pladema.violencereport.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.violencereport.domain.enums.EViolenceEvaluationRiskLevel;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportSituationDto {

	private Short situationId;

	private List<String> violenceTypes;

	private List<String> violenceModalities;

	private EViolenceEvaluationRiskLevel riskLevel;

	private DateDto initialDate;

	private DateDto lastModificationDate;

}
