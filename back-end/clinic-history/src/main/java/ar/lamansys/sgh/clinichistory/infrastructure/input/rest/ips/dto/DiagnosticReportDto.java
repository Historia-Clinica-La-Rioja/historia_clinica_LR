package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticReportDto extends ClinicalTermDto {
    private Integer healthConditionId;
    private String observations;
    private Long noteId;
    private String link;

    private HealthConditionDto healthCondition;
    private Integer encounterId;
    private Integer userId;
    private LocalDateTime effectiveTime;

    private List<FileDto> files;
}
