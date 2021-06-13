package net.pladema.hl7.dataexchange.model.adaptor;

import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FhirParam {

    public static final Character DELIMITER = '|';

    public static ReferenceParam newReferenceParam(String system, String value){
        return new ReferenceParam(FhirString.joining(DELIMITER, system, value));
    }

    public static TokenParam newTokenParam(String system, String value){
        return new TokenParam().setSystem(system).setValue(value);
    }

    public static TokenParam newTokenParam(String parameter) {
        if(parameter != null) {
            String[] values = parameter.split(scape(DELIMITER), 2);
            if(values.length > 1)
                return newTokenParam(values[0], values[1]);
        }
        return newTokenParam(parameter, " ");
    }

    public static String getParam(String system, String value){
        return FhirString.joining(DELIMITER, system, value);
    }

    public static String getIdentifier(String param){
        try {
            return param.substring(param.indexOf(DELIMITER)+1);
        }
        catch(NullPointerException ex) {
            return "-1";
        }
    }

    private static String scape(Character delimiter){
        return "\\".concat(delimiter.toString());
    }
}
