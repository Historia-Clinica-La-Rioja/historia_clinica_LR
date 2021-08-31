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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ConditionVo {

    private final Map<String, String> severityCoding;

    public ConditionVo() {
        severityCoding = new HashMap<>();
        severityCoding.put("LA6752-5", "mild");
        severityCoding.put("LA6751-7", "moderate");
        severityCoding.put("LA6750-9", "severe");
    }

    public ConditionVo(Integer id, String sctidCode, String sctidTerm, String clinicalStatus,
                       String verificationStatus, Date startDate, String severity, Date createdOn){
        this();
        setId(Cast.toString(id));
        setSctidCode(sctidCode);
        setSctidTerm(sctidTerm);
        setClinicalStatus(clinicalStatus);
        setVerificationStatus(verificationStatus);
        setStartDate(FhirDateMapper.toLocalDate(startDate));
        setSeverityCode(severity);
        setCreatedOn(FhirDateMapper.toLocalDateTime(createdOn));
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
        return new FhirCode(severityCode, severityCoding.get(severityCode));
    }

    public static FhirCode defaultClinicalStatus(){
        return new FhirCode(ResourceStatus.getDefault(Condition.SP_CLINICAL_STATUS));
    }

    public static FhirCode defaultVerificationStatus(){
        return new FhirCode(ResourceStatus.getDefault(Condition.SP_VERIFICATION_STATUS));
    }
}
