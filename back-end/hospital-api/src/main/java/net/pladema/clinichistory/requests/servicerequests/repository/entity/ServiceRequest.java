package net.pladema.clinichistory.requests.servicerequests.repository.entity;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.repository.entity.RequestIntentStatus;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_request")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ServiceRequest extends SGXAuditableEntity<Integer> {

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
	private String statusId = ServiceRequestStatus.ACTIVE;

	@Column(name = "intent_id", length = 20, nullable = false)
	private String intentId = RequestIntentStatus.ORDER;

	@Column(name = "category_id", length = 20, nullable = false)
	private String categoryId;

	@Column(name = "doctor_id", nullable = false)
	private Integer doctorId;

	@Column(name = "request_date")
	private LocalDateTime requestDate = LocalDateTime.now();

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "source_type_id")
	private Short sourceTypeId;

	@Column(name = "source_id")
	private Integer sourceId;

	public ServiceRequest(Integer institutionId,
						  Integer patientId,
						  Integer doctorId,
						  Integer medicalCoverageId,
						  String categoryId){
		super();
		this.institutionId = institutionId;
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.medicalCoverageId = medicalCoverageId;
		this.categoryId = categoryId;
	}
}
