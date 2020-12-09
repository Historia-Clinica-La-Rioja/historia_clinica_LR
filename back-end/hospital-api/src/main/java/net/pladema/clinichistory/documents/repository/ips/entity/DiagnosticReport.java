package net.pladema.clinichistory.documents.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DiagnosticReportStatus;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnostic_report")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiagnosticReport extends SGXAuditableEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "sctid_code", length = 20, nullable = false)
	private String sctidCode;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId = DiagnosticReportStatus.REGISTERED;

	@Column(name = "health_condition_id", nullable = false)
	private Integer healthConditionId;

	@Column(name = "effective_time", nullable = false)
	private LocalDateTime effectiveTime;

	@Column(name = "link", length = 255)
	private String link;

	@Column(name = "path_file", length = 255)
	private String pathFile;

	@Column(name = "note_id")
	private Long noteId;

}
