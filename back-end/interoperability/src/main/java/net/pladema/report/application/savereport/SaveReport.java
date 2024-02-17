package net.pladema.report.application.savereport;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.report.application.ReportWSStorage;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveReport {

	private final ReportWSStorage reportWSStorage;

	public void run(List<Map<String, Object>> reportData, String path) throws RestTemplateApiException {
		log.debug("Input parameters -> reportData {}, reportName {}", reportData, path);
		reportWSStorage.saveReport(reportData, path);
	}

}
