package net.pladema.clinichistory.requests.servicerequests.service.domain;

import lombok.*;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompleteDiagnosticReportBo {
    @Nullable
    private String observations;

    @Nullable
    private String link;
}
