package net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference;

import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CounterReferenceProcedureBo extends ClinicalTerm {

    public CounterReferenceProcedureBo(String sctid, String pt) {
        super(new SnomedBo(sctid, pt));
    }

}
