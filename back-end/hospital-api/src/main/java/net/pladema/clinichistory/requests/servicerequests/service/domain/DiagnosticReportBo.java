package net.pladema.clinichistory.requests.servicerequests.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticReportBo {
    private Integer healthConditionId;

    private String observations;

    private SnomedBo snomed;
}
