package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.domain.ConditionVo;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ConditionDto {

    public ConditionDto(ConditionVo conditionVo){
        this.id = conditionVo.getId();
        this.sctidCode = conditionVo.getSctidCode();
        this.sctidTerm = conditionVo.getSctidTerm();
        this.startDate = conditionVo.getStartDate();
        this.createdOn = conditionVo.getCreatedOn();
        this.clinicalStatus = new FhirCodeDto(conditionVo.getClinicalStatus());
        this.verificationStatus = new FhirCodeDto(conditionVo.getVerificationStatus());
        this.severityCode = new FhirCodeDto(conditionVo.getSeverity());


    }
    private String id;

    //Condition info
    private String sctidCode;
    private String sctidTerm;

    //Start date and record of the problem
    private LocalDate startDate;
    private LocalDateTime createdOn;

    private FhirCodeDto clinicalStatus;
    private FhirCodeDto verificationStatus;
    private FhirCodeDto severityCode;
}
