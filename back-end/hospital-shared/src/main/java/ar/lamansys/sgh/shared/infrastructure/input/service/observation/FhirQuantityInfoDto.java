package ar.lamansys.sgh.shared.infrastructure.input.service.observation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FhirQuantityInfoDto {

	private Integer id;
	private Float value;
	private String unit;
}
