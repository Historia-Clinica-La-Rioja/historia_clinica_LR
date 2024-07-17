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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Table(name = "institution_medicine_financing_status")
@Entity
@Where(clause = "deleted = false")
public class InstitutionMedicineFinancingStatus extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "medicine_id", nullable = false)
	private Integer medicineId;

	@Column(name = "financed", nullable = false)
	private Boolean financed;

	public InstitutionMedicineFinancingStatus(Integer institutionId, Integer medicineId){
		super();
		this.institutionId = institutionId;
		this.medicineId = medicineId;
		this.financed = Boolean.FALSE;
	}

}
