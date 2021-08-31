package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class HealthConditionBo extends ClinicalTerm {

    private String verificationId;

    private String verification;

    private boolean main = false;

    public HealthConditionBo() {
        super();
    }

    public HealthConditionBo(@Valid @NotNull(message = "{value.mandatory}") SnomedBo snomed) {
        super(snomed);
    }

    public String getVerificationId(){
        if (verificationId == null)
            return  ConditionVerificationStatus.CONFIRMED;
        return verificationId;
    }

    public boolean isError() {
        return getVerificationId().equalsIgnoreCase(ConditionVerificationStatus.ERROR);
    }

    public boolean isDiscarded() {
        return getVerificationId().equalsIgnoreCase(ConditionVerificationStatus.DISCARDED);
    }

    public boolean isActive(){
        return getStatusId().equals(ConditionClinicalStatus.ACTIVE);
    }
}
