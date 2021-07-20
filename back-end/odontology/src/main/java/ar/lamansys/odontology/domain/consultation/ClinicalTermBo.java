package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
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
public class ClinicalTermBo {

    private OdontologySnomedBo snomed;

    public ClinicalTermBo(String sctid, String pt) {
        this.snomed = new OdontologySnomedBo(sctid, pt);
    }

    public String getSctid() {
        return this.snomed.getSctid();
    }

    public String getPt() {
        return this.snomed.getPt();
    }

}
