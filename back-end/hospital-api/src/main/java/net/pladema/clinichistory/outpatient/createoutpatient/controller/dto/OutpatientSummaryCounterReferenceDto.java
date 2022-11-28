package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryProcedureDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OutpatientSummaryCounterReferenceDto {

    private Integer id;

    private String clinicalSpecialtyId;

    private String counterReferenceNote;

    private DateDto performedDate;

    private ProfessionalPersonDto professional;

    private List<ReferenceCounterReferenceFileDto> files;

    private List<CounterReferenceSummaryProcedureDto> procedures;

	private String institution;

	private String closureType;

}
