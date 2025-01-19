package net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.notification;

import lombok.Builder;

@Builder
public class VirtualConsultationAppointmentTemplateArgs {
	public final String patientName;
	public final String date;
	public final String professionalName;
	public final String clinicalSpecialty;
	public final String callLink;
}
