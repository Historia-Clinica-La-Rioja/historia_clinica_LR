package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.domain.AllergyIntoleranceVo;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
public class AllergyIntoleranceDto {

    public AllergyIntoleranceDto(AllergyIntoleranceVo allergyIntoleranceVo){
        this.id = allergyIntoleranceVo.getId();
        this.sctidCode = allergyIntoleranceVo.getSctidCode();
        this.sctidTerm = allergyIntoleranceVo.getSctidTerm();
        this.startDate = allergyIntoleranceVo.getStartDate();
        this.type = allergyIntoleranceVo.getType();
        this.categories = allergyIntoleranceVo.getCategories();
        this.criticality = allergyIntoleranceVo.getCriticality();
        this.clinicalStatus = new FhirCodeDto(allergyIntoleranceVo.getClinicalStatus());
        this.verificationStatus = new FhirCodeDto(allergyIntoleranceVo.getVerificationStatus());
    }

    private String id;

    //Allergy-intolerance info
    private String sctidCode;
    private String sctidTerm;

    private LocalDate startDate;

    private String type;
    private Set<String> categories=new HashSet<>();
    private String criticality;

    private FhirCodeDto clinicalStatus;
    private FhirCodeDto verificationStatus;

}
