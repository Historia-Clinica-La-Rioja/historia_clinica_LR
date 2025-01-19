package ar.lamansys.sgh.clinichistory.application.isolationalerts.exceptions;

import lombok.Getter;

@Getter
public class IsolationAlertException extends RuntimeException {
	private static final String PREFIX = "isolation-alert-exception";
	public static final String DOC_NOT_FOUND = String.format("%s.%s", PREFIX, "document-not-found");
	public static final String HC_NOT_FOUND = String.format("%s.%s", PREFIX, "health-condition-not-found");
	public static final String ALERT_NOT_FOUND = String.format("%s.%s", PREFIX, "alert-not-found");
	public static final String FINALIZED_ERROR = String.format("%s.%s", PREFIX, "finalized");
	public static final String ALREADY_FINALIZED_ERROR = String.format("%s.%s", PREFIX, "already-finalized");
	public static final String REQUIRED_OBSERVATIONS = String.format("%s.%s", PREFIX, "required-observations");

	private String code;

	private IsolationAlertException(String message, String code) {
		super(message);
		this.code = code;
	}

	public static IsolationAlertException documentNotFound(Long documentId) {
		return new IsolationAlertException(
			String.format("No se encontró el documento con id %s", documentId),
			DOC_NOT_FOUND
		);
	}

    public static IsolationAlertException healthConditionNotFound(Long documentId, String sctid, String pt) {
		return new IsolationAlertException(
				String.format("No se encontró el diagnostico (%s, %s) en el documento con id %s", sctid, pt, documentId),
				HC_NOT_FOUND
		);
    }

	public static IsolationAlertException alertNotFound(Integer alertId) {
		return new IsolationAlertException(
				String.format("No se encontró la alerta con id %s", alertId),
				ALERT_NOT_FOUND
		);
	}

	public static IsolationAlertException finalizedError(Integer alertId) {
		return new IsolationAlertException(
				String.format("No se pudo finalizar la alerta con id %s", alertId),
				FINALIZED_ERROR
		);
	}

	public static IsolationAlertException alreadyFinalized(Integer alertId) {
		return new IsolationAlertException(
				String.format("La alerta con id %s ya se encuentra finalizada", alertId),
				FINALIZED_ERROR
		);
	}

    public static IsolationAlertException requiredObservations(Integer alertId) {
		return new IsolationAlertException(
				String.format("Las observaciones son obligatorias cuando el tipo es 'Otros'"),
				REQUIRED_OBSERVATIONS
		);
    }
}
