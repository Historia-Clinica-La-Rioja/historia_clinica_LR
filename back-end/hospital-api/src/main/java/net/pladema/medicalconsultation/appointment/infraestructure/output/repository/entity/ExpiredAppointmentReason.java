package net.pladema.medicalconsultation.appointment.infraestructure.output.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expired_appointment_reason")
@Entity
public class ExpiredAppointmentReason implements Serializable {

	@Id
	@Column(name = "appointment_id", nullable = false)
	private Integer appointmentId;

	@Column(name = "type_id")
	private Short typeId;

	@Column(name = "reason", columnDefinition = "TEXT")
	private String reason;

}