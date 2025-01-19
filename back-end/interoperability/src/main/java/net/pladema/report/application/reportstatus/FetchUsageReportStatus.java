package net.pladema.report.application.reportstatus;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.report.application.ReportWSStorage;
import net.pladema.report.domain.UsageReportStatusBo;
import net.pladema.report.domain.exceptions.UsageReportStatusException;

@RequiredArgsConstructor
@Slf4j
@Service
public class FetchUsageReportStatus {

	private final ReportWSStorage reportWSStorage;

	public UsageReportStatusBo run() throws UsageReportStatusException {
		try {
			String healthCheckResult = reportWSStorage.healthCheck();
			return new UsageReportStatusBo(
					!ReportWSStorage.HEALTH_CHECK_DISABLED.equals(healthCheckResult)
			);
		} catch (RestTemplateApiException e) {
			throw new UsageReportStatusException(e.getStatusCode().toString(), e);
		} catch (Exception e) {
			throw new UsageReportStatusException(e.getMessage(), e);
		}

	}

}
