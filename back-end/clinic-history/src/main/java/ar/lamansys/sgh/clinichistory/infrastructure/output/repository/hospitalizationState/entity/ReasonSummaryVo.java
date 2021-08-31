package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReasonSummaryVo {

    @NotNull
    private Snomed snomed;

    private Integer consultationID;

    public ReasonSummaryVo(String id, String description, Integer consultationID){
        this.snomed = new Snomed(id, description, null, null);
        this.consultationID = consultationID;
    }

    public String getId() {
        return snomed.getSctid();
    }

    public String getPt() {
        return snomed.getPt();
    }
}
