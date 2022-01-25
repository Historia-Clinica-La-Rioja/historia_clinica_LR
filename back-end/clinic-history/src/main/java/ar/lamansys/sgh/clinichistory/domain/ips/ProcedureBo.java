package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
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

    public ProcedureBo(ProcedureSummaryBo procedureSummaryBo){
        this.setSnomed(procedureSummaryBo.getSnomed());
        this.performedDate = procedureSummaryBo.getPerformedDate();
    }

    public ProcedureBo(SnomedBo snomed) {
        super(snomed);
    }
}
