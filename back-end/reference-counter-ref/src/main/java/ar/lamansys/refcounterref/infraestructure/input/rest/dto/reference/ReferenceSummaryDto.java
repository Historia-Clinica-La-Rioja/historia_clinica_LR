package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReferenceSummaryDto {

	private Integer id;

	private String institution;

	private DateDto date;

	private String professionalFullName;

}
