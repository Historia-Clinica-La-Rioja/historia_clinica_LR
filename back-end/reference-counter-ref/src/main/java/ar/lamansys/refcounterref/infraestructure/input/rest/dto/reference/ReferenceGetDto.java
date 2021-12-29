package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import ar.lamansys.sgh.shared.infrastructure.input.service.CareLineDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceSummaryNoteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceGetDto implements Serializable {

    private static final long serialVersionUID = 6514576451162248752L;

    private Integer id;

    private DateDto referenceDate;

    private ReferenceSummaryNoteDto note;

    private CareLineDto careLine;

    private ClinicalSpecialtyDto clinicalSpecialty;

    private ProfessionalPersonDto professional;

    private List<ReferenceProblemDto> problems;

    private List<ReferenceCounterReferenceFileDto> files;

}
