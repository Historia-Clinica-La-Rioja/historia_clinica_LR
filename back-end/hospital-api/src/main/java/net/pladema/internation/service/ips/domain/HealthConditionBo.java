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

    public boolean isError() {
        return verificationId.equalsIgnoreCase(ConditionVerificationStatus.ERROR);
    }

    public boolean isDiscarded() {
        return verificationId.equalsIgnoreCase(ConditionVerificationStatus.DISCARDED);
    }
}
