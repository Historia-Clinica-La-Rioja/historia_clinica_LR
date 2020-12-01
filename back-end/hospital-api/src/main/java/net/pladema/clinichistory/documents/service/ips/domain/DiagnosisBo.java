package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionVerificationStatus;

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
