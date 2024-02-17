package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "historic_incharge_nurse_bed")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class HistoricInchargeNurseBed extends SGXAuditableEntity<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "incharge_nurse_id")
	private Integer inchargeNurseId;

	@Column(name = "bed_id")
	private Integer bedId;

	@Column(name = "until_date")
	private LocalDateTime untilDate;

	public HistoricInchargeNurseBed(Integer inchargeNurseId, Integer bedId, Integer createdBy) {
		this.inchargeNurseId = inchargeNurseId;
		this.bedId = bedId;
		this.setCreatedBy(createdBy);
		this.setUpdatedBy(createdBy);
		this.setCreatedOn(LocalDateTime.now());
		this.setUpdatedOn(LocalDateTime.now());
	}
}
