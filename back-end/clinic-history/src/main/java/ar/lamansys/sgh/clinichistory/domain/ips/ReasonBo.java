package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReasonSummaryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Reason;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReasonBo extends ClinicalTerm {

    public String getSctid() {
        return getSnomed().getSctid();
    }

    public String getPt() {
        return getSnomed().getPt();
    }

    public ReasonBo(ReasonSummaryBo reasonSummaryBo){
        super(reasonSummaryBo.getSnomed());
    }

    public ReasonBo(SnomedBo snomedBo) {
        super(snomedBo);
    }

    public ReasonBo(Reason reason){
        super(new SnomedBo(reason.getId(), reason.getDescription()));

    }
}
