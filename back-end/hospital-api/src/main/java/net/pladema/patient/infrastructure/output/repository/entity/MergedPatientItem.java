package net.pladema.patient.infrastructure.output.repository.entity;

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

@Entity
@Table(name = "merged_patient_item")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(SGXAuditListener.class)
public class MergedPatientItem extends SGXAuditableEntity<Integer> {

	public MergedPatientItem(String mergedTableName, String mergedIdName, Integer mergedIdValue, Integer oldPatientId, Integer newPatientId) {
		this.mergedTableName = mergedTableName;
		this.mergedIdName = mergedIdName;
		this.mergedIdValue = mergedIdValue;
		this.oldPatientId = oldPatientId;
		this.newPatientId = newPatientId;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "merged_table_name", nullable = false, length = 100)
	private String mergedTableName;

	@Column(name = "merged_id_name", nullable = false, length = 50)
	private String mergedIdName;

	@Column(name = "merged_id_value", nullable = false)
	private Integer mergedIdValue;

	@Column(name = "old_patient_id", nullable = false)
	private Integer oldPatientId;

	@Column(name = "new_patient_id", nullable = false)
	private Integer newPatientId;


}
