package net.pladema.report.application;

import java.util.List;
import java.util.Map;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;

public interface ReportWSStorage {
	String HEALTH_CHECK_DISABLED = "DISABLED";

	void saveReport(List<Map<String, Object>> reportData, String reportName);

	String healthCheck() throws RestTemplateApiException;

}
