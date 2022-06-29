package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "appointment_block_motive")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AppointmentBlockMotive {

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "description", nullable = false, length = 50)
	private String description;

}
