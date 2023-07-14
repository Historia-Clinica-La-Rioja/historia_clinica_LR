package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "details_order_image")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DetailsOrderImage {

	@Id
	@Column(name = "appointment_id")
	private Integer appointmentId;

	@Column(name = "observations")
	private String observations;

	@Column(name = "completed_on", nullable = false)
	private LocalDateTime completedOn;

	@Column(name = "completed_by", nullable = false)
	private Integer userId;

	@Column(name = "report_required", nullable = false)
	private Boolean isReportRequired;

	public DetailsOrderImage(Integer appointmentId, String observations, LocalDateTime completedOn, Integer userId, Boolean isReportRequired) {
		this.appointmentId = appointmentId;
		this.observations = observations;
		this.completedOn = completedOn;
		this.userId = userId;
		this.isReportRequired = isReportRequired;
	}

}
