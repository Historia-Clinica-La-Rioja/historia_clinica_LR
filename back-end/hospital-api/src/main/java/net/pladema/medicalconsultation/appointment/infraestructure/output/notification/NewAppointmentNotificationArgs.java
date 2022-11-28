package net.pladema.medicalconsultation.appointment.infraestructure.output.notification;

import lombok.Builder;

@Builder
public class NewAppointmentNotificationArgs {
	public final String professionalFullName;
	public final String specialty;
	public final String day;
	public final String time;
	public final String institution;
	public final String recomendation;
	public final String cancelationLink;
	public final String fromFullName;
	public final String medicalCoverage;
	public final String doctorOffice;
	public final String identificationNumber;

}
