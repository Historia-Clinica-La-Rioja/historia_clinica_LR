package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.*;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticReportDto {
    private SnomedDto snomed;
    private SnomedDto healthCondition;

    @Nullable
    private String observations;

    @Nullable
    private String link;
    private String statusId;
}
