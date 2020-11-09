package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.Cast;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.foundation.lifecycle.ResourceStatus;
import org.hl7.fhir.r4.model.Condition;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ConditionVo {

    private final Map<String, String> SEVERITY;

    public ConditionVo() {
        //TODO: add to DB
        SEVERITY = new HashMap<>();
        SEVERITY.put("LA6752-5", "mild");
        SEVERITY.put("LA6751-7", "moderate");
        SEVERITY.put("LA6750-9", "severe");
    }

    public ConditionVo(Object[] tuple){
        this();
        int index=0;
        setId(Cast.toString(tuple[index++]));
        setSctidCode(Cast.toString(tuple[index++]));
        setSctidTerm(Cast.toString(tuple[index++]));
        setClinicalStatus(Cast.toString(tuple[index++]));
        setVerificationStatus(Cast.toString(tuple[index++]));
        setStartDate(FhirDateMapper.toLocalDate(Cast.toSqlDate(tuple[index++])));
        setCreatedOn(FhirDateMapper.toLocalDateTime(Cast.toSqlTimestamp(tuple[index])));
    }

    private String id;

    //Condition info
    private String sctidCode;
    private String sctidTerm;

    //Start date and record of the problem
    private LocalDate startDate;
    private LocalDateTime createdOn;

    private String clinicalStatus;
    private String verificationStatus;
    private String severityCode;

    public FhirCode get(){
        return new FhirCode(sctidCode, sctidTerm);
    }

    public FhirCode getClinicalStatus(){
        return new FhirCode(ResourceStatus.getStatus(clinicalStatus));
    }

    public FhirCode getVerificationStatus(){
        return new FhirCode(ResourceStatus.getStatus(verificationStatus));
    }

    public FhirCode getSeverity(){
        return new FhirCode(severityCode, SEVERITY.get(severityCode));
    }

    public static FhirCode defaultClinicalStatus(){
        return new FhirCode(ResourceStatus.getDefault(Condition.SP_CLINICAL_STATUS));
    }

    public static FhirCode defaultVerificationStatus(){
        return new FhirCode(ResourceStatus.getDefault(Condition.SP_VERIFICATION_STATUS));
    }
}
