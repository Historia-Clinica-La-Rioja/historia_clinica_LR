package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionVerificationStatus;

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
