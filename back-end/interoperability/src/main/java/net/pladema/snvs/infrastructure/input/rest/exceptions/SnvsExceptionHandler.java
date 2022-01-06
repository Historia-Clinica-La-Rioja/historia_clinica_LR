package net.pladema.snvs.infrastructure.input.rest.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snvs.application.ports.event.exceptions.SnvsStorageException;
import net.pladema.snvs.application.ports.report.exceptions.ReportPortException;
import net.pladema.snvs.application.reportproblems.exceptions.ReportProblemException;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.problem.exceptions.SnvsProblemBoException;
import net.pladema.snvs.domain.report.exceptions.ReportCommandBoException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.snvs")
@Slf4j
public class SnvsExceptionHandler {

	@ExceptionHandler({ ReportPortException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ApiErrorMessageDto handleReportPortException(ReportPortException ex) {
		log.error("ReportPortException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ExceptionHandler({ ReportProblemException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ApiErrorMessageDto handleReportProblemException(ReportProblemException ex) {
		log.error("ReportProblemException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ExceptionHandler({ ReportCommandBoException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ApiErrorMessageDto handleReportCommandBoException(ReportCommandBoException ex) {
		log.error("ReportCommandBoException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ExceptionHandler({ SnvsEventInfoBoException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ApiErrorMessageDto handleSnvsEventInfoBoException(SnvsEventInfoBoException ex) {
		log.error("SnvsEventInfoBoException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ExceptionHandler({ SnvsStorageException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ApiErrorMessageDto handleSnvsStorageException(SnvsStorageException ex) {
		log.error("SnvsStorageException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ExceptionHandler({ SnvsProblemBoException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ApiErrorMessageDto handleSnvsProblemBoException(SnvsProblemBoException ex) {
		log.error("SnvsProblemBoException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}
}

