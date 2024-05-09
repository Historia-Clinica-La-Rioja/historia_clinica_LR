package net.pladema.hl7.dataexchange.exceptions;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class DiagnosticReportException extends RuntimeException{

	private DiagnosticReportExceptionEnum code;
	private HttpStatus status;

	public DiagnosticReportException(DiagnosticReportExceptionEnum code, HttpStatus status, String message) {
		super(message);
		this.status = status;
		this.code = code;
	}

	public String getCode() {
		return code.name();
	}
}
