package net.pladema.hl7.dataexchange.model.adaptor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FhirCode {

    private String theCode;
    private String theDisplay;

    public FhirCode(String theCode){
        this(theCode, null);
    }

    public FhirCode(String theCode, String theDisplay){
        this.theCode=theCode;
        this.theDisplay=theDisplay;
    }

}
