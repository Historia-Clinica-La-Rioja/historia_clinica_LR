package net.pladema.clinichistory.ips.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionVerificationStatus;

@Getter
@Setter
@ToString
public class HealthConditionBo extends ClinicalTerm {

    private String verificationId;

    private String verification;

    private boolean main = false;

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
