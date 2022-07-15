package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication;


import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "historic_nursing_record")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class HistoricNursingRecordStatus extends SGXAuditableEntity<HistoricNursingRecordStatusPK> implements Serializable {

	@EmbeddedId
	private HistoricNursingRecordStatusPK pk;

	@Column(name = "status_id", nullable = false)
	private Short statusId;

	@Column(name = "reason")
	private String reason;

	public HistoricNursingRecordStatus(Integer nursingRecordId ,Short statusId, String reason){
		this.pk = new HistoricNursingRecordStatusPK(nursingRecordId, LocalDateTime.now());
		this.statusId = statusId;
		this.reason = reason;
	}

	public HistoricNursingRecordStatusPK getId(){return this.pk;}

}
