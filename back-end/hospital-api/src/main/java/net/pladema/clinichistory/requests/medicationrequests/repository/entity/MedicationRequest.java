package net.pladema.clinichistory.requests.medicationrequests.repository.entity;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.*;
import net.pladema.clinichistory.requests.repository.entity.RequestIntentStatus;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "medication_request")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationRequest extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "medical_coverage_id")
	private Integer medicalCoverageId;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId = MedicationRequestStatus.ACTIVE;

	@Column(name = "intent_id", length = 20, nullable = false)
	private String intentId = RequestIntentStatus.ORDER;

	@Column(name = "category_id", length = 20, nullable = false)
	private String categoryId = MedicationRequestCategory.COMMUNITY;

	@Column(name = "doctor_id", nullable = false)
	private Integer doctorId;

	@Column(name = "has_recipe", nullable = false)
	private Boolean hasRecipe = false;

	@Column(name = "request_date")
	private LocalDate requestDate;

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "is_post_dated")
	private Boolean isPostDated;

	@Column(name = "repetitions")
	private Integer repetitions;

	@Column(name = "clinical_specialty_id")
	private Integer clinicalSpecialtyId;

	@Column(name = "is_archived")
	private Boolean isArchived;

	@Column(name = "uuid")
	private UUID uuid;


	public MedicationRequest(Integer patientId, Integer institutionId, String statusId,
							 String intentId, String categoryId, Integer doctorId, Boolean hasRecipe) {
		this.patientId = patientId;
		this.institutionId = institutionId;
		this.statusId = statusId;
		this.intentId = intentId;
		this.categoryId = categoryId;
		this.doctorId = doctorId;
		this.hasRecipe = hasRecipe;
	}
}
