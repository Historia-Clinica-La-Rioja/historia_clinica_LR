package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication;


import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "nursing_record")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NursingRecord extends SGXAuditableEntity<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "indication_id", nullable = false)
	private Integer indicationId;

	@Column(name = "scheduled_administration_time")
	private LocalDateTime scheduledAdministrationTime;

	@Column(name = "event", length = 100)
	private String event;

	@Column(name = "status_id", nullable = false)
	private Short statusId;

	@Column(name = "observation")
	private String observation;

	@Column(name = "administration_time")
	private LocalDateTime administrationTime;

}
