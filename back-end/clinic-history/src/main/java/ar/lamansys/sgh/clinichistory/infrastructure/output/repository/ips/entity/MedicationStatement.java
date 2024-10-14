package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "medication_statement")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationStatement extends SGXAuditableEntity<Integer> implements SGXDocumentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Transient
	private final Integer MEDICATION_STATEMENT_DUE_DATE = 30;

	@Transient
	private final Short MEDICATION_STATEMENT_INITIAL_STATE = 1;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Column(name = "cie10_codes")
	private String cie10Codes;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId = MedicationStatementStatus.ACTIVE;

	@Column(name = "health_condition_id")
	private Integer healthConditionId;

	@Column(name = "dosage_id")
	private Integer dosageId;

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "prescription_date")
	private LocalDate prescriptionDate;

	@Column(name = "is_digital")
	private Boolean isDigital;

	@Column(name = "prescription_line_number")
	private Integer prescriptionLineNumber;

	@Column(name = "prescription_line_state")
	private Short prescriptionLineState;

	@Column(name = "uuid")
	private UUID uuid;

	@Column(name = "suggested_commercial_medication_snomed_id")
	private Integer suggestedCommercialMedicationSnomedId;

	public MedicationStatement(Integer patientId, Integer snomedId, String statusId, Long noteId,
							   Integer healthConditionId, Integer dosageId) {
		super();
		this.patientId = patientId;
		this.snomedId = snomedId;
		if (statusId != null)
			this.statusId = statusId;
		this.noteId = noteId;
		this.healthConditionId = healthConditionId;
		this.dosageId = dosageId;
	}

	public MedicationStatement(Integer patientId, Integer snomedId, String statusId, Long noteId,
							   Integer healthConditionId, Integer dosageId, Integer prescriptionLineNumber, Boolean isDigital, LocalDate prescriptionDate, LocalDate dueDate) {
		super();
		this.patientId = patientId;
		this.snomedId = snomedId;
		if (statusId != null)
			this.statusId = statusId;
		this.noteId = noteId;
		this.healthConditionId = healthConditionId;
		this.dosageId = dosageId;
		this.prescriptionLineNumber = prescriptionLineNumber;
		this.isDigital = isDigital;
		this.prescriptionDate = prescriptionDate;
		this.dueDate = dueDate;
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

	@PrePersist
	public void setPrecalculatedData() {
		this.prescriptionLineState = MEDICATION_STATEMENT_INITIAL_STATE;
		if (this.isDigital == null)
			this.isDigital = false;
		if (this.dueDate == null)
			this.dueDate = LocalDate.now();
		if (this.prescriptionDate == null)
			this.prescriptionDate = LocalDate.now();
	}

}
