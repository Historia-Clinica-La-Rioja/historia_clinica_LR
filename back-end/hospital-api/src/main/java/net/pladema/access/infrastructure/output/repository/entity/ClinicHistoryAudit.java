package net.pladema.access.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.access.domain.bo.ClinicHistoryAccessBo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "clinic_history_audit")
@Entity
public class ClinicHistoryAudit {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "observations", columnDefinition = "TEXT")
	private String observations;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "access_date", nullable = false)
	private LocalDateTime accessDate = LocalDateTime.now();

	@Column(name = "reason_id", nullable = false)
	private Short reasonId;

	@Column(name = "scope")
	private Short scope;

	public ClinicHistoryAudit(ClinicHistoryAccessBo clinicHistoryAccessBo) {
		this.observations = clinicHistoryAccessBo.getObservations();
		this.reasonId = clinicHistoryAccessBo.getReasonId();
		this.scope = clinicHistoryAccessBo.getScope();
	}
}
