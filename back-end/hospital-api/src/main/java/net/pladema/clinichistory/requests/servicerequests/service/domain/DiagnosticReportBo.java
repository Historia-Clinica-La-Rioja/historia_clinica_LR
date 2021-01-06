package net.pladema.clinichistory.requests.servicerequests.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.documents.service.ips.domain.ClinicalTerm;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticReportBo extends ClinicalTerm {
    private Integer healthConditionId;
    private String observations;
    private Long noteId;
    private HealthConditionBo healthCondition;
    private Integer encounterId;
    private Integer userId;
    private LocalDateTime effectiveTime;
}
