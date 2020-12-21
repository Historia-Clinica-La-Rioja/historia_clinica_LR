package net.pladema.clinichistory.requests.servicerequests.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiagnosticReportFilterBo {
    Integer patientId;
    Integer status;
    String serviceRequest;
    String healthCondition;
}
