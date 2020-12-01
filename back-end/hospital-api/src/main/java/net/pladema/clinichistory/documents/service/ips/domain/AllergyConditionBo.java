package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.generalstate.domain.AllergyConditionVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllergyConditionBo extends HealthConditionBo {

    private String categoryId;

    private String severityId;

    private LocalDate date;

    public AllergyConditionBo(AllergyConditionVo allergyConditionVo) {
        super();
        setId(allergyConditionVo.getId());
        setStatusId(allergyConditionVo.getStatusId());
        setStatus(allergyConditionVo.getStatus());
        setSnomed(new SnomedBo(allergyConditionVo.getSnomed()));
        setCategoryId(allergyConditionVo.getCategoryId());
        setVerificationId(allergyConditionVo.getVerificationId());
        setVerification(allergyConditionVo.getVerification());
        setSeverityId(allergyConditionVo.getSeverityId());
    }
}
