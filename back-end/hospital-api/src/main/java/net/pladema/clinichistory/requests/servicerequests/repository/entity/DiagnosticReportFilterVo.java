package net.pladema.clinichistory.requests.servicerequests.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiagnosticReportFilterVo {
    Integer patientId;
    Integer status;
    String serviceRequest;
    String healthCondition;
}
