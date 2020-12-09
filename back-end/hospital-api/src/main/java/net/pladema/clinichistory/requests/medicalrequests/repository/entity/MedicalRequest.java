package net.pladema.clinichistory.requests.medicalrequests.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "medical_request")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicalRequest extends SGXAuditableEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "doctor_id", nullable = false)
	private Integer doctorId;

	@Column(name = "request_date")
	private LocalDate requestDate;

	@Column(name = "note_id")
	private Long noteId;




}
