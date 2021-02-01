package net.pladema.clinichistory.requests.servicerequests.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiagnosticReportFilterVo {
    private final Integer patientId;
    private final String status;
    private final String study;
    private final String healthCondition;
    private final String category;
}
