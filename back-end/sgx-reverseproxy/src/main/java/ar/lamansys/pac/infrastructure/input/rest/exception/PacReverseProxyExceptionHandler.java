package ar.lamansys.pac.infrastructure.input.rest.exception;

//import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys")
public class PacReverseProxyExceptionHandler {

/*	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler({ DashboardInfoException.class })
	protected ApiErrorMessageDto handleDashboardInfoException(DashboardInfoException ex) {
		log.debug("DashboardInfoException message -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler({ ChartDefinitionFetchException.class })
	protected ApiErrorMessageDto handleChartDefinitionFetchException(ChartDefinitionFetchException exception) {
		log.debug("ChartDefinitionFetchException message -> {}", exception.getMessage(), exception);
		return new ApiErrorMessageDto(
				null,
				exception.getMessage(),
				Map.of(
						"exception", exception
				)
		);
	*/
}
