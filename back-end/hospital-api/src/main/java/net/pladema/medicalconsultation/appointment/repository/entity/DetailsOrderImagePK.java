package net.pladema.medicalconsultation.appointment.repository.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DetailsOrderImagePK implements Serializable {
	@Column(name = "appointment_id", nullable = false)
	private Integer appointmentId;
	@Column(name = "role_id", nullable = false)
	private Short roleId;
}
