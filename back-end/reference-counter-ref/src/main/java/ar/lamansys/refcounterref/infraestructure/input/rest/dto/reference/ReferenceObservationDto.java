package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReferenceObservationDto {

	private String createdBy;

	private String observation;

	private DateTimeDto date;

}
