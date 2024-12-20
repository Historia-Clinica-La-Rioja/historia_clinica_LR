package ar.lamansys.refcounterref.domain.counterreference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import ar.lamansys.refcounterref.domain.professionalperson.ProfessionalPersonBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CounterReferenceSummaryBo {

    private Integer id;

    private LocalDate performedDate;

	private Integer authorPersonId;

    private String authorFullName;

    private String clinicalSpecialty;

    private String note;

    private List<ReferenceCounterReferenceFileBo> files;

    private List<CounterReferenceProcedureBo> procedures;

	private String institution;

	private EReferenceClosureType closureType;

	private LocalDateTime createdOn;

    public CounterReferenceSummaryBo(Integer id, LocalDate performedDate, Integer authorPersonId,
                                     String clinicalSpecialty, String note, String institution,
									 Short closureTypeId, LocalDateTime createdOn) {
        this.id = id;
        this.performedDate = performedDate;
        this.clinicalSpecialty = clinicalSpecialty;
        this.note = note;
        this.authorPersonId = authorPersonId;
    	this.institution = institution;
		this.closureType = EReferenceClosureType.getById(closureTypeId);
		this.createdOn = createdOn;
	}

}
