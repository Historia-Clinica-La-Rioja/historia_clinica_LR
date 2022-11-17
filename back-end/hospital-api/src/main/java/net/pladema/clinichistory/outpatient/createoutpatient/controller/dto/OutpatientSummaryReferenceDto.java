package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutpatientSummaryReferenceDto {

    private Integer id;

    private String careLine;

    private String clinicalSpecialty;

    private String note;
    
    private List<ReferenceCounterReferenceFileDto> files;

    private OutpatientSummaryCounterReferenceDto counterReference;

	private String institution;

}
