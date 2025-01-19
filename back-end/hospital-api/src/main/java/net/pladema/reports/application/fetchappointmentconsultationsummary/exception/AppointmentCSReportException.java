package net.pladema.reports.application.fetchappointmentconsultationsummary.exception;

public class AppointmentCSReportException extends RuntimeException {

	private final EAppointmentCSReportException code;

	public AppointmentCSReportException(EAppointmentCSReportException code, String message) {
		super(message);
		this.code = code;
	}
}
