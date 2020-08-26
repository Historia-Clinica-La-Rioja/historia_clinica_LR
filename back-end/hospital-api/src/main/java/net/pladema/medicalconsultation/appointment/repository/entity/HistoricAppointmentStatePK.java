package net.pladema.medicalconsultation.appointment.repository.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HistoricAppointmentStatePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3658828638453755263L;

	@Column(name = "appointment_id", nullable = false)
	private Integer appointmentId;

	@Column(name = "changed_state_date")
	private LocalDateTime changedStateDate;
}
