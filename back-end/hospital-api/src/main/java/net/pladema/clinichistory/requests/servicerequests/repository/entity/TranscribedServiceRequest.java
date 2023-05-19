	package net.pladema.clinichistory.requests.servicerequests.repository.entity;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.repository.entity.RequestIntentStatus;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "transcribed_service_request")
@Getter
@Setter
@ToString
public class TranscribedServiceRequest {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "study_id", nullable = false)
	private Integer studyId;
	@Column(name = "healthcare_professional_name", nullable = false)
	private String healthcareProfessionalName;

	@Column(name = "institution_name")
	private String institutionName;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "creation_date")
	private LocalDateTime creationDate = LocalDateTime.now();

	public TranscribedServiceRequest(Integer studyId, String healthcareProfessionalName, String institutionName, Integer patientId){
		super();
		this.studyId = studyId;
		this.healthcareProfessionalName = healthcareProfessionalName;
		this.institutionName = institutionName;
		this.patientId = patientId;
	}
}
