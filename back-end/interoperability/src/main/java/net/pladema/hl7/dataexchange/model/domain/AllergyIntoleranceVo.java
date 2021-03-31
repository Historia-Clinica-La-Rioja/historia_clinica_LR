package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.Cast;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.foundation.lifecycle.ResourceStatus;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.hl7.fhir.r4.model.AllergyIntolerance;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class AllergyIntoleranceVo {

    private final BidiMap<Short,String> typeCoding;
    private final Map<String, String> categoryCoding;
    private final BidiMap<Short, String> criticalityCoding;

    public AllergyIntoleranceVo(){
        typeCoding = new DualHashBidiMap<>();
        typeCoding.put((short)1, "allergy");
        typeCoding.put((short)2, "intolerance");

        categoryCoding = new HashMap<>();
        categoryCoding.put("414285001", "food");
        categoryCoding.put("416098002", "medication");
        categoryCoding.put("426232007", "environment");
        categoryCoding.put("402591008", "biologic");

        criticalityCoding = new DualHashBidiMap<>();
        criticalityCoding.put((short)1, "low");
        criticalityCoding.put((short)2, "high");
        criticalityCoding.put((short)3, "unable-to-assess");
    }

    public AllergyIntoleranceVo(Integer id, String sctidCode, String sctidTerm,
                                String clinicalStatus, String verificationStatus,
                                String category, Date startDate){
        this();
        setId(Cast.toString(id));
        setSctidCode(sctidCode);
        setSctidTerm(sctidTerm);
        setClinicalStatus(Cast.toString(clinicalStatus));
        setVerificationStatus(Cast.toString(verificationStatus));
        setCategories(category);
        setStartDate(FhirDateMapper.toLocalDate(startDate));
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
        return typeCoding.getOrDefault(type, null);
    }

    public Set<String> setCategories(Set<String> categories){
        categories.forEach(c-> categories.add(categoryCoding.get(c)));
        return categories;
    }

    private void setCategories(String category){
        categories.add(categoryCoding.get(category));
    }

    public boolean noInformationAboutCategories(){
        return categories.isEmpty();
    }

    public String getCriticality(){
        return criticalityCoding.getOrDefault(criticality, null);
    }

    public FhirCode getClinicalStatus(){
        return new FhirCode(ResourceStatus.getStatus(clinicalStatus));
    }

    public FhirCode getVerificationStatus(){
        return new FhirCode(ResourceStatus.getStatus(verificationStatus));
    }

    public void setCriticality(String criticality){
        this.criticality = criticalityCoding.getKey(criticality);
    }

    public void setType(String type){
        this.type = typeCoding.getKey(type);
    }

    public static FhirCode defaultClinicalStatus() {
        return new FhirCode(ResourceStatus.getDefault(AllergyIntolerance.SP_CLINICAL_STATUS));
    }
}
