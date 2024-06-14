package net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CreateOutpatientConsultationServiceRequestException extends Exception {

	private String code;
	private Map<String, Object> params;

	private CreateOutpatientConsultationServiceRequestException(String code, String domainObjectName, String domainObjectValue) {
		this.code = code;
		this.params = new HashMap<>();
		this.params.put(domainObjectName, domainObjectValue);
	}

	private CreateOutpatientConsultationServiceRequestException(String code, Map<String, Object> params) {
		this.code = code;
		this.params = params;
	}

	public static CreateOutpatientConsultationServiceRequestException diagnosticReportCreationFailed(Integer serviceRequestId, Integer outpatientConsultationId) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceRequestId", serviceRequestId);
		params.put("outpatientConsultationId", outpatientConsultationId);
		return new CreateOutpatientConsultationServiceRequestException(
			"diagnostic-report-creation-failed",
			params
		);
	}

	public static CreateOutpatientConsultationServiceRequestException invalidDiagnosticReportTemplateChange(Integer outpatientConsultationId, Integer diagnosticReportId) {
		Map<String, Object> params = new HashMap<>();
		params.put("diagnosticReportId", diagnosticReportId);
		params.put("outpatientConsultationId", outpatientConsultationId);
		return new CreateOutpatientConsultationServiceRequestException(
				"invalid-diagnostic-report-template-change",
				params
		);
	}

	public static CreateOutpatientConsultationServiceRequestException diagnosticReportObservationException(String code,
		String domainObjectName, Object domainObjectValue, Integer outpatientConsultationId
	)
	{
		Map<String, Object> params = new HashMap<>();
		params.put(domainObjectName, domainObjectValue);
		params.put("outpatientConsultationId", outpatientConsultationId);
		return new CreateOutpatientConsultationServiceRequestException(
				code,
				params
		);
	}

	public String getCode() {
		return String.format("%s.%s", "create-outpatient-consultation-service-request", code);
	}

}