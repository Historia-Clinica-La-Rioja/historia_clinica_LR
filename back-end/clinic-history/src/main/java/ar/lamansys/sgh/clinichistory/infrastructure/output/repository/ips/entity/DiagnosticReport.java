package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Table(name = "diagnostic_report")
@Entity
public class DiagnosticReport extends SGXAuditableEntity<Integer> implements SGXDocumentEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Column(name = "cie10_codes", length = 255, nullable = true)
	private String cie10Codes;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId = DiagnosticReportStatus.REGISTERED;

	@Column(name = "health_condition_id", nullable = false)
	private Integer healthConditionId;

	@Column(name = "effective_time", nullable = false)
	private LocalDateTime effectiveTime = LocalDateTime.now();

	@Column(name = "link", length = 255)
	private String link;

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "uuid")
	private UUID uuid;

	public DiagnosticReport(Integer patientId, Integer snomedId, String cie10Codes,
							Long noteId, Integer healthConditionId) {
		super();
		this.patientId = patientId;
		this.snomedId = snomedId;
		this.cie10Codes = cie10Codes;
		this.noteId = noteId;
		this.healthConditionId = healthConditionId;
	}

	public DiagnosticReport(Integer patientId, Integer snomedId, String cie10Codes,
							Long noteId, Integer healthConditionId, String statusId) {
		super();
		this.patientId = patientId;
		this.snomedId = snomedId;
		this.cie10Codes = cie10Codes;
		this.noteId = noteId;
		this.healthConditionId = healthConditionId;
		if (statusId!=null) {this.statusId = statusId;}
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DiagnosticReport that = (DiagnosticReport) o;
		return id.equals(that.id) &&
				patientId.equals(that.patientId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, patientId);
	}

	public boolean isFinal() {
		return Objects.equals(this.getStatusId(), DiagnosticReportStatus.FINAL);
	}
}
