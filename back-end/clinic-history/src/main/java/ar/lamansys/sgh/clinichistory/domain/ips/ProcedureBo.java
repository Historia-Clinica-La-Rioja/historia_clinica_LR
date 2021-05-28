package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.entity.ProcedureSummaryVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProcedureBo extends ClinicalTerm {

    private LocalDate performedDate;

    public ProcedureBo(ProcedureSummaryVo procedureSummaryVo){
        this.setSnomed(procedureSummaryVo.getSnomed());
        this.performedDate = procedureSummaryVo.getPerformedDate();
    }

    public ProcedureBo(SnomedBo snomed) {
        super(snomed);
    }
}
