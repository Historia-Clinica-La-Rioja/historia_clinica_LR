package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
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
        return snomed.getId();
    }

    public String getPt() {
        return snomed.getPt();
    }

    public ReasonBo(ReasonSummaryVo reasonSummaryVo){
        this.snomed = new SnomedBo(reasonSummaryVo.getSnomed());
    }
}
