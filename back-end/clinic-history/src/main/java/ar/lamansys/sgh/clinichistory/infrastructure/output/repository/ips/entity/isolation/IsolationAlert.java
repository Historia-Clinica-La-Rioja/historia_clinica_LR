package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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

import java.time.LocalDate;
@Entity
@Table(name = "isolation_alert")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IsolationAlert extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	@Column(name = "health_condition_id", nullable = false)
	private Integer healthConditionId;
	//See EIsolationCriticality
	@Column(name = "isolation_criticality_id", nullable = false)
	private Short isolationCriticalityId;
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;
	@Column(name = "observations", nullable = true)
	private String observations;
	//See EisolationStatus
	@Column(name = "isolation_status_id", nullable = false)
	private Short isolationStatusId;
	/**
	 * When parentId is null: This is a new alert created from an evolution note.
	 * Otherwise: This alert is a copy of an existing alert, possibly with some changes. The parentId carries
	 * the id of the alert being modified.
	 */
	@Column(name = "parent_id", nullable = true)
	private Integer parentId;
}
