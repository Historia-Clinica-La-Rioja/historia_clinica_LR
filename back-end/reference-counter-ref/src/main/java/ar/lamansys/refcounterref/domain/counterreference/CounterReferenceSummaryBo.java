package ar.lamansys.refcounterref.domain.counterreference;

import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import ar.lamansys.refcounterref.domain.professionalperson.ProfessionalPersonBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CounterReferenceSummaryBo {

    private Integer id;

    private LocalDate performedDate;

    private ProfessionalPersonBo professional;

    private String clinicalSpecialty;

    private String note;

    private List<ReferenceCounterReferenceFileBo> files;

    private List<CounterReferenceProcedureBo> procedures;

    public CounterReferenceSummaryBo(Integer id, LocalDate performedDate, Integer professionalId,
                                     String professionalName, String professionalLastName,
                                     String clinicalSpecialty, String note) {
        this.id = id;
        this.performedDate = performedDate;
        this.clinicalSpecialty = clinicalSpecialty;
        this.note = note;
        this.professional = new ProfessionalPersonBo(professionalId, professionalName, professionalLastName);
    }

}
