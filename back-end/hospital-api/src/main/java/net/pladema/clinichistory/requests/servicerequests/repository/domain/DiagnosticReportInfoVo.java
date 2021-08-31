package net.pladema.clinichistory.requests.servicerequests.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiagnosticReportInfoVo {
    private Integer id;
    private SnomedBo snomed;
    private String statusId;
    private String status;
    private HealthConditionBo healthCondition;
    private String note;
    private Integer encounterId;
}
