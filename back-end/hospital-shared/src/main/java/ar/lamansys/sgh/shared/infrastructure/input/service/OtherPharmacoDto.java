package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OtherPharmacoDto {

	@NotNull(message = "{value.mandatory}")
	private SharedSnomedDto snomed;

	@NotNull(message = "{value.mandatory}")
	private NewDosageDto dosage;

}
