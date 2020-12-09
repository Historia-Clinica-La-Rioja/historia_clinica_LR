package net.pladema.clinichistory.requests.servicerequests.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.repository.entity.RequestIntentStatus;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "service_request")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ServiceRequest extends SGXAuditableEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "doc_id", nullable = false)
	private Long docId;

	@Column(name = "medical_coverage_id")
	private Integer medicalCoverageId;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId = ServiceRequestStatus.ACTIVE;

	@Column(name = "intent_id", length = 20, nullable = false)
	private String intentId = RequestIntentStatus.ORDER;

	@Column(name = "category_id", length = 20, nullable = false)
	private String categoryId;

	@Column(name = "doctor_id", nullable = false)
	private Integer doctorId;

	@Column(name = "request_date")
	private LocalDate requestDate;

	@Column(name = "note_id")
	private Long noteId;




}
