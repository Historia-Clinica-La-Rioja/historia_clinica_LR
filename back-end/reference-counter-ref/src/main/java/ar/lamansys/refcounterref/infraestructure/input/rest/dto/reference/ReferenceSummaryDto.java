package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReferenceSummaryDto {

	private Integer referenceId;

	private InstitutionInfoDto institution;

	private DateDto date;

	private String professionalFullName;

}
