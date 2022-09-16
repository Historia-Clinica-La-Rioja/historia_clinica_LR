package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalPersonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CounterReferenceSummaryDto implements Serializable {

    private static final long serialVersionUID = 132835028098042674L;

    private Integer id;

    private LocalDate performedDate;

    private ProfessionalPersonDto professional;

    private String clinicalSpecialty;

    private String note;

    private List<ReferenceCounterReferenceFileDto> files;

    private List<CounterReferenceSummaryProcedureDto> procedures;

	private String institution;

	private String closureType;

}
