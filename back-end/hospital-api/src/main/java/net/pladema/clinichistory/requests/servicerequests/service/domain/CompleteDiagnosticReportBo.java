package net.pladema.clinichistory.requests.servicerequests.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.annotation.Nullable;

@Getter
@Setter
@ToString
public class CompleteDiagnosticReportBo {
    @Nullable
    private String observations;

    @Nullable
    private String link;

    @Nullable
    private String filePath;
}
