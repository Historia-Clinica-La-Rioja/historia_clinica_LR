package ar.lamansys.sgx.cubejs.infrastructure.api.exception;

import ar.lamansys.sgx.cubejs.application.dashboardinfo.excepciones.DashboardInfoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys.sgx.cubejs")
public class DashboardExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardExceptionHandler.class);

	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler({ DashboardInfoException.class })
	protected ApiErrorMessageDto handleDashboardInfoException(DashboardInfoException ex, Locale locale) {
		LOG.debug("InputValidationException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}


}

