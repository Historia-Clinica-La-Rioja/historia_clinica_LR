	package net.pladema.clinichistory.requests.servicerequests.repository.entity;

	import java.time.LocalDateTime;

	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.Table;

	import lombok.AllArgsConstructor;
	import lombok.Getter;
	import lombok.NoArgsConstructor;
	import lombok.Setter;
	import lombok.ToString;

@Entity
@Table(name = "transcribed_service_request")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
		this.studyId = studyId;
		this.healthcareProfessionalName = healthcareProfessionalName;
		this.institutionName = institutionName;
		this.patientId = patientId;
	}
}
