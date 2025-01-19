package net.pladema.clinichistory.requests.servicerequests.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.domain.ips.FileBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiagnosticReportResultsBo extends ClinicalTerm {

    private Integer healthConditionId;
    private String observations;
    private Long noteId;
    private String link;
    private HealthConditionBo healthCondition;
    private Integer encounterId;
    private Integer userId;
    private LocalDateTime effectiveTime;
    private String category;
    private String source;
    private List<FileBo> files;
    private Integer sourceId;
    private String observationsFromServiceRequest;

    public String getDiagnosticReportSnomedPt() {
        return this.getSnomed().getPt();
    }

    public String getSnomedPt() {
        return this.healthCondition.getSnomedPt();
    }
}
