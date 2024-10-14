package ar.lamansys.online.infraestructure.notification.message;

import lombok.Builder;

@Builder
public class ConfirmarReservaNotificationArgs {
	public final String cancelationLink;
	public final String namePatient;
	public final String date;
	public final String nameProfessional;
	public final String specialty;
	public final String institution;
	public final String recomendation;
}
