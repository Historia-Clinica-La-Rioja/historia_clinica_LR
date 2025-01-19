package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentOrderDetailImageBO {

	private Integer idDoctor;

	private Integer idServiceRequest;

	private String observations;

	private String professionalOrderTranscribed;

	private LocalDateTime creationDate;

	private String healthCondition;


}
