package net.pladema.reports.application.fetchnominalemergencycarepisodedetail.exception;

import lombok.Getter;

@Getter
public class NominalECEDetailReportException extends RuntimeException {

	private final NominalECEDetailReportExceptionEnum code;

	public NominalECEDetailReportException(NominalECEDetailReportExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
