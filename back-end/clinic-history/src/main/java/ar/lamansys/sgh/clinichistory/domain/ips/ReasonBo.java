package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.entity.ReasonSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Reason;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
