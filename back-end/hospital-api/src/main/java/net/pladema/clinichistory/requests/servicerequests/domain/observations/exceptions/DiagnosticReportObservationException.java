package net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class DiagnosticReportObservationException extends Exception {

	private String code;
	/**
	 * Domain object's name linked to this exception
	 */
	private String domainObjectName;
	/**
	 * Domain object's id linked to this exception
	 */
	private Object domainObjectId;

	public DiagnosticReportObservationException(String code) {
		this.code = code;
		this.domainObjectName = "";
		this.domainObjectId = "";
	}

	public String getCode() {
		return String.format("%s.%s", "diagnostic-report-observation", code);
	}

	public Map<String, Object> getParams() {
		return Map.of(getDomainObjectName(), getDomainObjectId());
	}

}
