package net.pladema.medicine.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
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
@Table(name = "medicine_group_problem")
@EntityListeners(SGXAuditListener.class)
@Entity
public class MedicineGroupProblem extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="problem_id", nullable = false)
	private Integer problemId;

	@Column(name="medicine_group_id", nullable = false)
	private Integer medicineGroupId;

}
