package net.pladema.internation.service.ips.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.masterdata.entity.ConditionVerificationStatus;

@Getter
@Setter
@ToString
public class DiagnosisBo extends HealthConditionBo {

    private boolean presumptive = false;

    public boolean isPresumptive() {
        if (getVerificationId() != null && ConditionVerificationStatus.isDownState(getVerificationId()))
            return false;
        return presumptive;
    }
}
