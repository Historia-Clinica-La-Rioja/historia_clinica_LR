package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionVerificationStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiagnosisBo extends HealthConditionBo {

    private boolean presumptive = false;

    public DiagnosisBo(SnomedBo snomed) {
        super(snomed);
    }

    public boolean isPresumptive() {
        if (getVerificationId() != null && ConditionVerificationStatus.isDownState(getVerificationId()))
            return false;
        return presumptive;
    }
}
