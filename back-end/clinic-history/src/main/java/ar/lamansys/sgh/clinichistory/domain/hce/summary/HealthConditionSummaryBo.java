package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Integer consultationId;

    private List<ReferenceSummaryBo> references;

    public HealthConditionSummaryBo(Integer id, Integer patientId, SnomedBo snomed, String statusId,
                                    String status, String cie10codes, LocalDate startDate,
                                    LocalDate inactivationDate, boolean main, String problemId, Integer consultationId) {
        super(id, patientId, snomed, statusId, status, cie10codes);
        this.startDate = startDate;
        this.inactivationDate = inactivationDate;
        this.main = main;
        this.problemId = problemId;
        this.consultationId = consultationId;
    }

}
