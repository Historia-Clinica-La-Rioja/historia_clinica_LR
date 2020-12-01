package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.ips.domain.ClinicalTerm;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.outpatient.repository.domain.ProcedureSummaryVo;

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
