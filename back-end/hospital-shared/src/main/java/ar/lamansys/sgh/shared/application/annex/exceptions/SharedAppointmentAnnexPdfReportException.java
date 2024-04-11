package ar.lamansys.sgh.shared.application.annex.exceptions;

import lombok.Getter;

@Getter
public class SharedAppointmentAnnexPdfReportException extends Exception {
	private String code;
	private String message;
	public SharedAppointmentAnnexPdfReportException(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
