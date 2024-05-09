package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FhirObservationBo {

	private Integer id;
	private Integer observationGroupId;
	private String loincCode;
	private String value;
	private FhirQuantityBo quantity;

}
