package ar.lamansys.sgh.publicapi.documents.annex.application.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class FetchAnnexReportByEncounterException extends Exception {
	String code;
	String message;
	Throwable cause;

	public FetchAnnexReportByEncounterException(String code, String message, Throwable cause){
		this.code = code;
		this.message = message;
		this.cause = cause;
	}
	public FetchAnnexReportByEncounterException(String code, String message){
		this.code = code;
		this.message = message;
	}

	public static FetchAnnexReportByEncounterException appointmentNotFound(String refsetCode, Integer encounterId) {
		return new FetchAnnexReportByEncounterException(
			"appointment-not-found",
			String.format("El encuentro no existe. Data: refsetCode %s, encounterId %s", refsetCode, encounterId));
	}

	public static FetchAnnexReportByEncounterException scopeNotSupported(String scope) {
		return new FetchAnnexReportByEncounterException(
				"scope-not-supported",
				String.format("El scope %s no es soportado", scope));
	}

	public static FetchAnnexReportByEncounterException reportFailed(String refsetCode, Integer encounterId, Exception e) {
		return new FetchAnnexReportByEncounterException(
				"report-failed",
				String.format("No se pudo generar el reporte. Data: refsetCode %s, encounterId %s", refsetCode, encounterId),
				e);
	}
}
