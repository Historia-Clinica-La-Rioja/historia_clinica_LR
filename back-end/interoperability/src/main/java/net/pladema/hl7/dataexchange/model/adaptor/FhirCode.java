package net.pladema.hl7.dataexchange.model.adaptor;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FhirCode implements Serializable {

    private static final long serialVersionUID = -8410142260416237047L;

    private String theCode;
    private String theDisplay;

    public FhirCode(String theCode){
        this(theCode, null);
    }

    public FhirCode(String theCode, String theDisplay){
        this.theCode=theCode;
        this.theDisplay=theDisplay;
    }

    public boolean hasCode(){
        return theCode != null || theDisplay != null;
    }

}
