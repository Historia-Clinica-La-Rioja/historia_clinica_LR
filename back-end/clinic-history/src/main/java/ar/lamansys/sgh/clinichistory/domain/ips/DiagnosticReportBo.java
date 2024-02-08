package ar.lamansys.sgh.clinichistory.domain.ips;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

	private Integer sourceId;

    public DiagnosticReportBo(Integer id, Integer encounterId, Integer healthConditionId, String healthConditionSctid, String healthConditionPt,
                              String cie10Codes, String diagnosticReportSctid, String diagnosticReportPt, String observations) {
        this.setId(id);
        this.encounterId = encounterId;
        this.healthConditionId = healthConditionId;
        this.healthCondition = new HealthConditionBo(new SnomedBo(healthConditionSctid, healthConditionPt));
        this.healthCondition.setId(this.healthConditionId);
        this.healthCondition.setCie10codes(cie10Codes);
        this.setSnomed(new SnomedBo(diagnosticReportSctid, diagnosticReportPt));
        this.observations = observations;
    }

    public String getDiagnosticReportSnomedPt() {
        return this.getSnomed().getPt();
    }

	public String getSnomedPt() {
		return this.healthCondition.getSnomedPt();
	}
}
