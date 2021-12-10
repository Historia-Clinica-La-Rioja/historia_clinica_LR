package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import ar.lamansys.sgh.shared.infrastructure.input.service.CareLineDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
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
public class ReferenceDto implements Serializable {

    private static final long serialVersionUID = -3184712067120494370L;

    private Integer id;

    private DateDto referenceDate;

    private ReferenceNoteDto note;

    private CareLineDto careLine;

    private ClinicalSpecialtyDto clinicalSpecialty;

    private ProfessionalPersonDto professional;

    private List<ReferenceProblemDto> problems;

    private List<ReferenceFileDto> files;

}
