package net.pladema.medicalconsultation.virtualConsultation.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VirtualConsultationNotificationDataBo {

	private String patientName;

	private String patientSelfPerceivedName;

	private String patientLastName;

	private Short priorityId;

	private LocalDateTime creationDateTime;

	private String responsibleFirstName;

	private String responsibleLastName;

	private Integer responsibleUserId;

	private String clinicalSpecialty;

	private String institutionName;

	private String callId;

}
