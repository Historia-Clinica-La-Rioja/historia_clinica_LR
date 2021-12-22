package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.ReferenceSummaryBo;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HealthConditionSummaryBo extends ClinicalTerm {

    private LocalDate startDate;

    private LocalDate inactivationDate;

    private boolean main;

    private String problemId;

    private List<ReferenceSummaryBo> references;

    private Integer consultationID;

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
