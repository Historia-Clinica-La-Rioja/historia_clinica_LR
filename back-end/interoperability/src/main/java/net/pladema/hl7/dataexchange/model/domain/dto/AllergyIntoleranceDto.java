package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.domain.AllergyIntoleranceVo;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class AllergyIntoleranceDto {

    public AllergyIntoleranceDto(AllergyIntoleranceVo allergyIntoleranceVo){
        this.id = allergyIntoleranceVo.getId();
        this.sctidCode = allergyIntoleranceVo.getSctidCode();
        this.sctidTerm = allergyIntoleranceVo.getSctidTerm();
        this.startDate = allergyIntoleranceVo.getStartDate();
        this.type = allergyIntoleranceVo.getType();
        this.category = allergyIntoleranceVo.getCategory();
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
    private String category;
    private String criticality;

    private FhirCodeDto clinicalStatus;
    private FhirCodeDto verificationStatus;

}
