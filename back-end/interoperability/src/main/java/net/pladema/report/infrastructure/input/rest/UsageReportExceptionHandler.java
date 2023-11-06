package net.pladema.report.infrastructure.input.rest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.report.domain.exceptions.SaveReportException;
import net.pladema.report.domain.exceptions.UsageReportStatusException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.report")
public class UsageReportExceptionHandler {

	@ExceptionHandler({ UsageReportStatusException.class })
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	protected ApiErrorMessageDto handleUsageReportStatusException(UsageReportStatusException ex) {
		log.error("UsageReportStatusException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto("bad-connection", ex.getMessage());
	}

	@ExceptionHandler({ SaveReportException.class })
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	protected ApiErrorMessageDto handleSaveReportException(SaveReportException ex) {
		log.error("SaveReportException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto("bad-request", ex.getMessage());
	}
}

