package net.pladema.reports.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AnnexReportException extends RuntimeException {
	private String code;
	private String reason;
	private String errorDetails;
}
