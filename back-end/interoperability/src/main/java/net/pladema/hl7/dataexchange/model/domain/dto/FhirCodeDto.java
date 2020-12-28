package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;

@Getter
public class FhirCodeDto {

    public FhirCodeDto(FhirCode fhirCode){
        this.theCode = fhirCode.getTheCode();
        this.theDisplay = fhirCode.getTheDisplay();
    }
    private final String theCode;
    private final String theDisplay;
}
