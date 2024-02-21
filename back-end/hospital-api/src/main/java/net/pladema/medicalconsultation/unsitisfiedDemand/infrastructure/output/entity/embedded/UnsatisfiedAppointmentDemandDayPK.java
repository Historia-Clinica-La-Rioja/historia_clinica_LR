package net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.entity.embedded;

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
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UnsatisfiedAppointmentDemandDayPK implements Serializable {

	private static final long serialVersionUID = -3321507939709690504L;

	@Column(name = "unsatisfied_appointment_demand_id")
	private Integer UnsatisfiedAppointmentDemandId;

	@Column(name = "week_day_id")
	private Short weekDayId;

}
