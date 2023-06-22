package net.pladema.report.infrastructure.input.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import net.pladema.report.application.ReportWSStorage;
import net.pladema.report.application.reportstatus.FetchUsageReportStatus;
import net.pladema.report.application.sendreport.SendUsageReport;
import net.pladema.report.domain.exceptions.SaveReportException;
import net.pladema.report.domain.exceptions.UsageReportStatusException;
import net.pladema.report.infrastructure.input.rest.dto.UsageReportStatusDto;
import net.pladema.report.service.UseReportService;

@ExtendWith(MockitoExtension.class)
class UsageReportControllerTest {
	private UsageReportController controller;

	@Mock
	private ReportWSStorage reportWSStorage;

	@Mock
	private UseReportService useReportService;

	@BeforeEach
	void setUp() {
		this.controller = new UsageReportController(
				new FetchUsageReportStatus(reportWSStorage),
				new SendUsageReport(useReportService)
		);
	}

	@Test
	void getStatus_whenNotEnabled() {
		var status = getStatusWithHealthCheck(ReportWSStorage.HEALTH_CHECK_DISABLED);
		assertThat(status.domainId)
				.isNotNull();
		assertThat(status.isAllowedToSend)
				.isFalse();
	}

	@Test
	void getStatus_whenReadyToSend() {
		var status = getStatusWithHealthCheck(null);
		assertThat(status.domainId)
				.isNotNull();
		assertThat(status.isAllowedToSend)
				.isTrue();
	}

	@Test
	void getStatus_whenConnectionError() throws RestTemplateApiException {

		UsageReportStatusException exception = getStatusWithError(
				new RestTemplateApiException(UNAUTHORIZED, "Error con la respuesta", null)
		);

		assertThat(exception.getMessage())
				.isEqualTo("401 UNAUTHORIZED");

	}

	@Test
	void getStatus_whenRuntimeError() throws RestTemplateApiException {
		UsageReportStatusException exception = getStatusWithError(
						new RuntimeException("Error inesperado")
		);

		assertThat(exception.getMessage())
				.isEqualTo("Error inesperado");

	}

	@Test
	void sendReport() throws SaveReportException {
		var sent = controller.sendReport();
		assertThat(sent.code)
				.isEqualTo("Accepted");
	}

	@Test
	void sendReport_whenRuntimeError() throws SaveReportException {

		var exception = sendReportWithError(
				new RuntimeException("Error inesperado")
		);
		assertThat(exception.getMessage())
				.isEqualTo("Error inesperado");
	}

	private UsageReportStatusDto getStatusWithHealthCheck(String healthCheck) {
		try {
			when(reportWSStorage.healthCheck()).thenReturn(healthCheck);
			return controller.getStatus();
		} catch (RestTemplateApiException | UsageReportStatusException e) {
			throw new RuntimeException(e);
        }
    }

	private UsageReportStatusException getStatusWithError(Throwable error) throws RestTemplateApiException {
		when(reportWSStorage.healthCheck()).thenThrow(
				error
		);

		UsageReportStatusException exception = Assertions.assertThrows(UsageReportStatusException.class, () ->
				controller.getStatus()
		);
		return exception;
	}

	private SaveReportException sendReportWithError(Throwable error) throws SaveReportException {
		willThrow(error).given(useReportService).execute();

		SaveReportException exception = Assertions.assertThrows(SaveReportException.class, () ->
				controller.sendReport()
		);
		return exception;
	}
}