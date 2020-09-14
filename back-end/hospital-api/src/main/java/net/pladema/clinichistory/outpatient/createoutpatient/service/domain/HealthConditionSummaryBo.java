package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.ips.service.domain.ClinicalTerm;
import net.pladema.clinichistory.ips.service.domain.SnomedBo;
import net.pladema.clinichistory.outpatient.repository.domain.HealthConditionSummaryVo;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class HealthConditionSummaryBo extends ClinicalTerm {

    private LocalDate startDate;

    private LocalDate inactivationDate;

    private boolean main;

    private String problemId;

    public HealthConditionSummaryBo(HealthConditionSummaryVo healthConditionSummaryVo){
        this.setId(healthConditionSummaryVo.getId());
        this.setSnomed(new SnomedBo(healthConditionSummaryVo.getSnomed()));
        this.setStatusId(healthConditionSummaryVo.getStatusId());
        this.startDate = healthConditionSummaryVo.getStartDate();
        this.inactivationDate = healthConditionSummaryVo.getInactivationDate();
        this.main = healthConditionSummaryVo.isMain();
        this.problemId = healthConditionSummaryVo.getProblemId();
    }
}
