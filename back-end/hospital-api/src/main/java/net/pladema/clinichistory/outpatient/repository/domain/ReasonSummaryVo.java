package net.pladema.clinichistory.outpatient.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;

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
        return snomed.getId();
    }

    public String getPt() {
        return snomed.getPt();
    }
}
