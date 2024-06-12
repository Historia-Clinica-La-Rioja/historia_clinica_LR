package net.pladema.medicalconsultation.appointment.infraestructure.output.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointment_patient_identity_accreditation_status")
@Entity
public class AppointmentPatientIdentityAccreditationStatus implements Serializable {

	private static final long serialVersionUID = 1016039492233552268L;

	@Id
	@Column(name = "appointment_id")
	private Integer appointmentId;

	@Column(name = "patient_identity_accreditation_status_id")
	private Short patientIdentityAccreditationStatusId;

	@Column(name = "patient_identification_hash")
	private String patientIdentificationHash;

}
