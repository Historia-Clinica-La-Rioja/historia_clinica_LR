package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;

import java.io.Serializable;

@Getter
@Setter
public class MedicationIngredientVo implements Serializable {

    private static final long serialVersionUID = 3140670141305795660L;

    private String sctidCode;

    private String sctidTerm;

    private boolean active;

    private String unitMeasure;
    private Double unitValue;

    private String presentationUnit;
    private Double presentationValue;

    public FhirCode get() {
        return new FhirCode(sctidCode, sctidTerm);
    }
}
