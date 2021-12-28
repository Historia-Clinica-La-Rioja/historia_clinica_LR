package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.CounterReferenceSummaryProcedureDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.CounterReferenceProfessionalInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ReferenceCounterReferenceFileDto;
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

    private CounterReferenceProfessionalInfoDto professional;

    private List<ReferenceCounterReferenceFileDto> files;

    private List<CounterReferenceSummaryProcedureDto> procedures;

}
