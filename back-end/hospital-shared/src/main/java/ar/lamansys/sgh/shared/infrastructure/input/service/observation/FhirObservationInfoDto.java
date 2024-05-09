package ar.lamansys.sgh.shared.infrastructure.input.service.observation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FhirObservationInfoDto {

	private Integer id;
	private Integer observationGroupId;
	private String loincCode;
	private String value;
	private FhirQuantityInfoDto quantity;


}
