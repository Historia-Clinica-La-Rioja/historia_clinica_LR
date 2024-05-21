package net.pladema.medicine.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Table(name = "medicine_financing_status")
@Entity
public class MedicineFinancingStatus extends SGXAuditableEntity<Integer> implements Serializable {

	@Id
	@Column
	private Integer id;

	@Column(name = "financed", nullable = false)
	private Boolean financed;

	public MedicineFinancingStatus(Integer id){
		this.id = id;
		this.financed = false;
	}

}
