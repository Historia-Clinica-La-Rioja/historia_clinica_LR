package net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.entity.embedded.UnsatisfiedAppointmentDemandDayPK;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "unsatisfied_appointment_demand_day")
@Entity
public class UnsatisfiedAppointmentDemandDay implements Serializable {

	private static final long serialVersionUID = -3114456171317553353L;

	@EmbeddedId
	private UnsatisfiedAppointmentDemandDayPK pk;

}
