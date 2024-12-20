package ar.lamansys.refcounterref.infraestructure.input.rest.dto.counterreference;

import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ReferenceClosureSummaryDto {


	private String authorFullName;

	private DateTimeDto createdOn;

	private String note;

	private EReferenceClosureType closureType;

	private List<ReferenceCounterReferenceFileDto> files;

}
