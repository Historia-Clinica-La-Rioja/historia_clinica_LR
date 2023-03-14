package ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ReferenceAppointmentPk implements Serializable {

	@Column(name = "reference_id", nullable = false)
	private Integer referenceId;

	@Column(name = "appointment_id", nullable = false)
	private Integer appointmentId;

}
