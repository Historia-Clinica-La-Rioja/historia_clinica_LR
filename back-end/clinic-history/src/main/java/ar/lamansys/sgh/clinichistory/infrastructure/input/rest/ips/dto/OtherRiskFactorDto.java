package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OtherRiskFactorDto {

	private String bodyTemperatureDescription;

	private Boolean cryingExcessive;

	private String muscleHypertoniaDescription;

	private String respiratoryRetractionDescription;

	private Boolean stridor;

	private String perfusionDescription;

}
