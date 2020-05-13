package net.pladema.internation.service.ips.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.masterdata.entity.ConditionVerificationStatus;

@Getter
@Setter
@ToString
public class HealthConditionBo extends ClinicalTerm {

    private String verificationId;

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
}
