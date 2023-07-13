package net.pladema.medicalconsultation.appointment.repository.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "details_order_image")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DetailsOrderImage {

	@EmbeddedId
	private DetailsOrderImagePK pk;

	@Column(name = "observations")
	private String observations;

	@Column(name = "completed_on", nullable = false)
	private LocalDateTime completedOn;

	@Column(name = "completed_by", nullable = false)
	private Integer userId;

	@Column(name = "report_required", nullable = false)
	private Boolean isReportRequired;

	public DetailsOrderImage(Integer appointmentId, String observations, LocalDateTime completedOn, Integer userId, Short roleId, Boolean isReportRequired) {
		this.pk = new DetailsOrderImagePK(appointmentId, roleId);
		this.observations = observations;
		this.completedOn = completedOn;
		this.userId = userId;
		this.isReportRequired = isReportRequired;
	}

	public Integer getAppointmentId() {
		return this.pk.getAppointmentId();
	}

}
