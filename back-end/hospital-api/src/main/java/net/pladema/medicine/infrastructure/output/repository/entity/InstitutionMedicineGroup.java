package net.pladema.medicine.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
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

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Table(name = "institution_medicine_group")
@Entity
public class InstitutionMedicineGroup extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "medicine_group_id", nullable = false)
	private Integer medicineGroupId;

	public InstitutionMedicineGroup (Integer institutionId, Integer medicineGroupId){
		super();
		this.institutionId = institutionId;
		this.medicineGroupId = medicineGroupId;
	}

}
