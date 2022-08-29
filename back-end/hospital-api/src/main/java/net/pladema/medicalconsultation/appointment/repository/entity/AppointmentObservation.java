package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "appointment_observation")
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AppointmentObservation {

	@Id
	@Column(name = "appointment_id")
	private Integer appointmentId;

	@Column(name = "observation", columnDefinition = "TEXT")
	private String observation;

	@Column(name = "created_by")
	private Integer createdBy;

}
