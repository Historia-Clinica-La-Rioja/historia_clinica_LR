package ar.lamansys.sgh.shared.infrastructure.input.service.observation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FhirQuantityInfoDto {

	private Integer id;
	private BigDecimal value;
	private String unit;
}
