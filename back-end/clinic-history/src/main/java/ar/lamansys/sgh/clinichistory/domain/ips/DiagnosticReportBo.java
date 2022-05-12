package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticReportBo extends ClinicalTerm {
    private Integer healthConditionId;
    private String observations;
    private Long noteId;
    private String link;

    private HealthConditionBo healthCondition;
    private Integer encounterId;
    private Integer userId;
    private LocalDateTime effectiveTime;
	private String category;
	private String source;

    private List<FileBo> files;
}
