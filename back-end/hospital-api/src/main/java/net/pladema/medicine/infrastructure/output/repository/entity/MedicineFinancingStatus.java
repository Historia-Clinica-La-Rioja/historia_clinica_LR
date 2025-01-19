package net.pladema.medicine.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Table(name = "medicine_financing_status")
@Entity
@Where(clause = "deleted = false")
public class MedicineFinancingStatus extends SGXAuditableEntity<Integer> {

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
