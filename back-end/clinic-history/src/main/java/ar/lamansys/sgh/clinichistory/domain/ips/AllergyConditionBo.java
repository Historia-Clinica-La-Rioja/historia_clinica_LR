package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.AllergyConditionVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllergyConditionBo extends HealthConditionBo {

    private Short categoryId;

    private Short criticalityId;

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
        setCriticalityId(allergyConditionVo.getCriticalityId());
    }

    public AllergyConditionBo(SnomedBo snomedBo) {
        super(snomedBo);
    }
}
