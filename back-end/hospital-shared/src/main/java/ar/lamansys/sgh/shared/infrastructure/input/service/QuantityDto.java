package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityDto implements Serializable {

	@NotNull(message = "{value.mandatory}")
	private Integer value;

	@NotNull(message = "{value.mandatory}")
	private String unit;


}
