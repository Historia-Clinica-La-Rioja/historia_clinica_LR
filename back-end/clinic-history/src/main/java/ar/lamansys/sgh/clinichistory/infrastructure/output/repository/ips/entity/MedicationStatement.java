package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "medication_statement")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationStatement extends SGXAuditableEntity<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Column(name = "cie10_codes", length = 255, nullable = true)
	private String cie10Codes;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId = MedicationStatementStatus.ACTIVE;

	@Column(name = "health_condition_id")
	private Integer healthConditionId;

	@Column(name = "dosage_id")
	private Integer dosageId;

	@Column(name = "note_id")
	private Long noteId;

	public MedicationStatement(Integer patientId, Integer snomedId, String cie10Codes, String statusId, Long noteId,
							   Integer healthConditionId, Integer dosageId) {
		super();
		this.patientId = patientId;
		this.snomedId = snomedId;
		this.cie10Codes = cie10Codes;
		if (statusId != null)
			this.statusId = statusId;
		this.noteId = noteId;
		this.healthConditionId = healthConditionId;
		this.dosageId = dosageId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MedicationStatement that = (MedicationStatement) o;
		return id.equals(that.id) &&
				patientId.equals(that.patientId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, patientId);
	}
}
