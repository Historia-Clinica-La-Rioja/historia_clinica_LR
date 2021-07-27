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

@Getter
@Setter
public class AllergyIntoleranceVo {

    private final BidiMap<Short,String> typeCoding;
    private final BidiMap<Short, String> categoryCoding;
    private final BidiMap<Short, String> criticalityCoding;

    public AllergyIntoleranceVo(){
        typeCoding = new DualHashBidiMap<>();
        typeCoding.put((short)1, "allergy");
        typeCoding.put((short)2, "intolerance");

        categoryCoding = new DualHashBidiMap<>();
        categoryCoding.put((short)1, "food");
        categoryCoding.put((short)2, "medication");
        categoryCoding.put((short)3, "biologic");
        categoryCoding.put((short)4, "environment");


        criticalityCoding = new DualHashBidiMap<>();
        criticalityCoding.put((short)1, "low");
        criticalityCoding.put((short)2, "high");
        criticalityCoding.put((short)3, "unable-to-assess");
    }

    public AllergyIntoleranceVo(Integer id, String sctidCode, String sctidTerm,
                                String clinicalStatus, String verificationStatus,
                                Short category, Short criticality, Date startDate){
        this();
        setId(Cast.toString(id));
        setSctidCode(sctidCode);
        setSctidTerm(sctidTerm);
        setClinicalStatus(Cast.toString(clinicalStatus));
        setVerificationStatus(Cast.toString(verificationStatus));
        setCategory(category);
        setCriticality(criticality);
        setStartDate(FhirDateMapper.toLocalDate(startDate));
    }

    private String id;

    //Allergy-intolerance info
    private String sctidCode;
    private String sctidTerm;

    private LocalDate startDate;

    private Short type;
    private Short category;
    private Short criticality;

    private String clinicalStatus;
    private String verificationStatus;

    public FhirCode get(){
        return new FhirCode(sctidCode, sctidTerm);
    }

    public String getType(){
        return typeCoding.getOrDefault(type, null);
    }

    public String getCategory(){
        return this.categoryCoding.getOrDefault(this.category, null);
    }

    public void setCategory(Short categoryCode){
        this.category = categoryCode;
    }

    public void setCategory(String categoryTerm){
        this.category = this.categoryCoding.getKey(categoryTerm);
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

    public void setCriticality(Short criticality){
        this.criticality = criticality;
    }

    public void setType(String type){
        this.type = typeCoding.getKey(type);
    }

    public static FhirCode defaultClinicalStatus() {
        return new FhirCode(ResourceStatus.getDefault(AllergyIntolerance.SP_CLINICAL_STATUS));
    }
}
