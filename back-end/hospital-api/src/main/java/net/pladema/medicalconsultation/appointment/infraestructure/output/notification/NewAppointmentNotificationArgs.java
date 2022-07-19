package net.pladema.medicalconsultation.appointment.infraestructure.output.notification;

import lombok.Builder;

@Builder
public class NewAppointmentNotificationArgs {
	public final String professionalFullName;
	public final String specialty;
	public final String day;
	public final String time;
	public final String institution;
	public final String address;
	public final String recomendation;
	public final String cancelationLink;
	public final String fromFullName;
}
