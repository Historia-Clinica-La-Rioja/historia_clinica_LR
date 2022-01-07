package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HCEReferenceProblemBo {

    @Nullable
    private Integer id;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private SnomedBo snomed;

    public String getSnomedSctid() {
        return this.snomed.getSctid();
    }

    public String getSnomedPt() {
        return this.snomed.getPt();
    }

}
