package net.pladema.clinichistory.requests.servicerequests.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiagnosticReportFilterVo {
    private final Integer patientId;
    private final String status;
    private final String diagnosticReport;
    private final String healthCondition;
}
