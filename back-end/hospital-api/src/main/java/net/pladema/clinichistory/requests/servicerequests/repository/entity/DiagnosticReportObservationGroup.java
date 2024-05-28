package net.pladema.clinichistory.requests.servicerequests.repository.entity;


import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "diagnostic_report_observation_group")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiagnosticReportObservationGroup extends SGXAuditableEntity<Integer> {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "diagnostic_report_id", nullable = false)
	private Integer diagnosticReportId;
	@Column(name = "procedure_template_id", nullable = false)
	private Integer procedureTemplateId;

	@Column(name = "is_partial_upload", nullable = false)
	private Boolean isPartialUpload;

	public DiagnosticReportObservationGroup(Integer diagnosticReportId, Integer procedureTemplateId, Boolean isPartialUpload) {
		this.diagnosticReportId = diagnosticReportId;
		this.procedureTemplateId = procedureTemplateId;
		this.isPartialUpload = isPartialUpload;
	}
}
