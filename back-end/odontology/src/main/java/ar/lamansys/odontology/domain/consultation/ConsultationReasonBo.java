package ar.lamansys.odontology.domain.consultation;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConsultationReasonBo extends ClinicalTermBo {

    public ConsultationReasonBo(String sctid, String pt) {
        super(sctid, pt);
    }
}
