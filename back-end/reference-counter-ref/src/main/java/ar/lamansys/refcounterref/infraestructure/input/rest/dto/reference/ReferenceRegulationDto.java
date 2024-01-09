package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceRegulationDto {

	private Integer referenceId;

	private Integer ruleId;

	private String ruleLevel;

	private EReferenceRegulationState state;

	private String reason;

	private String professionalName;

	private DateTimeDto createdOn;

}
