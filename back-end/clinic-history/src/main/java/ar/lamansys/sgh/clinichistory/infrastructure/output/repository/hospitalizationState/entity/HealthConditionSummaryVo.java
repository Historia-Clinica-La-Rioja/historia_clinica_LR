package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HealthConditionSummaryVo extends ClinicalTermVo {

    private LocalDate startDate;

    private LocalDate inactivationDate;

    private boolean main;

    private String problemId;

    private Integer consultationID;

    public HealthConditionSummaryVo(Integer id, String snomedId, String snomedPt, String statusId, LocalDate startDate,
                                    LocalDate inactivationDate, Boolean main, String problemId, Integer consultationID){
        super(id, new Snomed(snomedId, snomedPt, null, null), statusId);
        this.startDate = startDate;
        this.inactivationDate = inactivationDate;
        this.main = main;
        this.problemId = problemId;
        this.consultationID = consultationID;
    }
}
