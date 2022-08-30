package net.pladema.medicalconsultation.appointment.repository.entity;

import java.io.Serializable;

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
public class DocumentAppointmentPK implements Serializable {

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "appointment_id", nullable = false)
	private Integer appointmentId;
}
