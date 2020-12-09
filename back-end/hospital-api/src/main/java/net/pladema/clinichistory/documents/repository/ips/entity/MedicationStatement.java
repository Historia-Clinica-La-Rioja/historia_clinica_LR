package net.pladema.clinichistory.documents.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "medication_statement")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationStatement extends SGXAuditableEntity {

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

	@Column(name = "sctid_code", length = 20, nullable = false)
	private String sctidCode;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId = MedicationStatementStatus.ACTIVE;

	@Column(name = "health_condition_id", nullable = true)
	private Integer healthConditionId;

	@Column(name = "chronic", nullable = false)
	private Boolean chronic = false;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "suspended_start_date")
	private LocalDate suspendedStartDate;

	@Column(name = "suspended_end_date")
	private LocalDate suspendedEndDate;

	@Column(name = "note_id")
	private Long noteId;

	public MedicationStatement(Integer patientId, String sctId, String statusId, Long noteId) {
		super();
		this.patientId = patientId;
		this.sctidCode = sctId;
		if (statusId != null)
			this.statusId = statusId;
		this.noteId = noteId;
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
