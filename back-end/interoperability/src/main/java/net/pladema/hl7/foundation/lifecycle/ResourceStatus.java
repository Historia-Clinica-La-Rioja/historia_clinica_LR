package net.pladema.hl7.foundation.lifecycle;

import org.hl7.fhir.r4.model.Condition;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResourceStatus {

    public static final String ENTERED_IN_ERROR="723510000";
    public static final String ACTIVE = "55561003";

    private static final Map<String, String> status = new HashMap<>();
    private static final Map<String, String> defaults = new HashMap<>();

    private ResourceStatus(){
        status.put(null,"");
        status.put(ACTIVE, "active");
        status.put("255594003","completed");
        status.put("59156000","confirmed");
        status.put(ENTERED_IN_ERROR,"entered-in-error");
        status.put("73425007", "inactive");
        status.put("18629005","intended");
        status.put("385660001", "not-done");
        status.put("266710000","not-taken");
        status.put("385655000","on-hold");

        status.put("260385009","refuted"); //from condition
        status.put("277022003", "remission");
        status.put("723506003", "resolved");
        status.put("6155003","stopped");
        status.put("261665006","unknown");

        status.put("76104008","unconfirmed"); //from allergy
        status.put("723511001","refuted"); //from allergy correcto según Snomed

        defaults.put(null, "");
        defaults.put("clinical-status", "55561003");
        defaults.put(Condition.SP_VERIFICATION_STATUS, "59156000");
    }

    public static String getStatus(String sctid){
        return status.get(sctid);
    }

    public static String getDefault(String sctid){
        if(sctid != null)
            return status.get(defaults.get(sctid));
        return null;
    }
}
