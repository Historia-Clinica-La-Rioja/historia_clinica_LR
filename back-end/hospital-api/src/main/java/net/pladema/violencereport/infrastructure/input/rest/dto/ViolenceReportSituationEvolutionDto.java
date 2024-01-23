package net.pladema.violencereport.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ViolenceReportSituationEvolutionDto {

	private Short situationId;

	private Short evolutionId;

	private DateDto episodeDate;

	private DateTimeDto createdOn;

	private String professionalFullName;

}
