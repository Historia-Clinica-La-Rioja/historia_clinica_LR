package net.pladema.medicalconsultation.appointment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "custom_appointment")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomAppointment extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Column(name = "appointment_id")
	private Integer appointmentId;

	@NotNull
	@Column(name = "repeat_every")
	private Short repeatEvery;

	@NotNull
	@Column(name = "day_week_id")
	private Short dayWeekId;

	@Nullable
	@Column(name = "end_date")
	private LocalDate endDate;

	public CustomAppointment(Integer appointmentId, Short repeatEvery, Short dayWeekId, LocalDate endDate) {
		this.appointmentId = appointmentId;
		this.repeatEvery = repeatEvery;
		this.dayWeekId = dayWeekId;
		this.endDate = endDate;
	}
}
