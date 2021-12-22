package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReasonSummaryBo {

    private SnomedBo snomed;

    private Integer consultationId;

    public ReasonSummaryBo(SnomedBo snomed, Integer consultationId){
        this.snomed = snomed;
        this.consultationId = consultationId;
    }

    public String getId() {
        return snomed.getSctid();
    }

    public String getPt() {
        return snomed.getPt();
    }
}
