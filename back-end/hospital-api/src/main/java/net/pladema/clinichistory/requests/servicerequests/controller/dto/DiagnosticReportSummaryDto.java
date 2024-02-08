package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DiagnosticReportSummaryDto {

    @NotNull(message = "${value.mandatory}")
    private Integer diagnosticReportId;

    @NotNull(message = "${value.mandatory}")
    private String pt;
}
