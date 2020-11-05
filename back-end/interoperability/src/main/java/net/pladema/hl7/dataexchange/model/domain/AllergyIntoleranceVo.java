package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import net.pladema.hl7.foundation.lifecycle.ResourceStatus;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.hl7.fhir.r4.model.AllergyIntolerance;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class AllergyIntoleranceVo {

    //TODO: add to DB
    private final BidiMap<Short,String> TYPE;
    private final Map<String, String> CATEGORY;
    //TODO: add to DB
    private final BidiMap<Short, String> CRITICALITY;

    public AllergyIntoleranceVo(){
        TYPE = new DualHashBidiMap<>();
        TYPE.put((short)1, "allergy");
        TYPE.put((short)2, "intolerance");

        CATEGORY = new HashMap<>();
        CATEGORY.put("414285001", "food");
        CATEGORY.put("416098002", "medication");
        CATEGORY.put("426232007", "environment");
        CATEGORY.put("402591008", "biologic");

        CRITICALITY = new DualHashBidiMap<>();
        CRITICALITY.put((short)1, "low");
        CRITICALITY.put((short)2, "high");
        CRITICALITY.put((short)3, "unable-to-assess");
    }

    private String id;

    //Allergy-intolerance info
    private String sctidCode;
    private String sctidTerm;

    private LocalDate startDate;

    private Short type;
    private final Set<String> categories=new HashSet<>();
    private Short criticality;

    private String clinicalStatus;
    private String verificationStatus;

    public FhirCode get(){
        return new FhirCode(sctidCode, sctidTerm);
    }

    public String getType(){
        return TYPE.getOrDefault(type, null);
    }

    public Set<String> setCategories(Set<String> categories){
        categories.forEach((c)-> categories.add(CATEGORY.get(c)));
        return categories;
    }

    public String getCriticality(){
        return CRITICALITY.getOrDefault(criticality, null);
    }

    public FhirCode getClinicalStatus(){
        return new FhirCode(ResourceStatus.getStatus(clinicalStatus));
    }

    public FhirCode getVerificationStatus(){
        return new FhirCode(ResourceStatus.getStatus(verificationStatus));
    }

    public void setCriticality(String criticality){
        this.criticality = CRITICALITY.getKey(criticality);
    }

    public void setType(String type){
        this.type = TYPE.getKey(type);
    }

    public static FhirCode defaultClinicalStatus() {
        return new FhirCode(ResourceStatus.getDefault(AllergyIntolerance.SP_CLINICAL_STATUS));
    }
}
