package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.service.domain.SnomedBo;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ReasonBo {

    @NotNull
    private SnomedBo snomed;

    public String getId() {
        return snomed.getId();
    }

    public String getPt() {
        return snomed.getPt();
    }
}
