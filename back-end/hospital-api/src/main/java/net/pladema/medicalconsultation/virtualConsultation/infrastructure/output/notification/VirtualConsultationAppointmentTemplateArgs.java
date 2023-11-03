package net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VirtualConsultationAppointmentTemplateArgs {

	private String patientName;

	private String date;

	private String professionalName;

	private String clinicalSpecialty;

	private String callLink;

}
