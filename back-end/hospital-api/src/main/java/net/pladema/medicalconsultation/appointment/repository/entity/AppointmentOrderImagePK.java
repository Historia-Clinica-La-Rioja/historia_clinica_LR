package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentOrderImagePK implements Serializable {

	@Column(name = "appointment_id", nullable = false)
	private Integer appointmentId;

	@Column(name = "order_id", nullable = false)
    private Integer orderId;


}
