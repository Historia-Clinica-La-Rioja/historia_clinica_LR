package net.pladema.medicalconsultation.appointment.service.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DetailsOrderImageBo {
	private Integer appointmentId;
	private String observations;
	private LocalDateTime completedOn;
	private Integer professionalId;
	private Boolean isReportRequired;
	private Integer patientId;
}
