package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FhirQuantityBo {

	private Integer id;
	private Float value;
	private String unit;

	static public FhirQuantityBo empty() {
		return new FhirQuantityBo();
	}
}