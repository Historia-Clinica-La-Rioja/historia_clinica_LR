package ar.lamansys.refcounterref.domain.clinicalterm;

import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReferenceCounterReferenceClinicalTermBo {

    private SnomedBo snomed;

    public ReferenceCounterReferenceClinicalTermBo(String sctid, String pt) {
        this.snomed = new SnomedBo(sctid, pt);
    }

    public String getSctid() {
        return this.snomed.getSctid();
    }

    public String getPt() {
        return this.snomed.getPt();
    }

}