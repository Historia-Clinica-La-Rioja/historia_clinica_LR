package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.entity.Reason;
import net.pladema.clinichistory.outpatient.repository.domain.ReasonSummaryVo;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReasonBo {

    @NotNull
    private SnomedBo snomed;

    public String getId() {
        return snomed.getSctid();
    }

    public String getPt() {
        return snomed.getPt();
    }

    public ReasonBo(ReasonSummaryVo reasonSummaryVo){
        this.snomed = new SnomedBo(reasonSummaryVo.getSnomed());
    }

    public ReasonBo(Reason reason){
        this.snomed = new SnomedBo(reason.getId(), reason.getDescription());

    }
}
