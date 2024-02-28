package net.pladema.reports.imageNetworkProductivity.infrastructure.input.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;

import net.pladema.reports.imageNetworkProductivity.application.exception.WrongDateException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.reports.imageNetworkProductivity")
public class ImageNetworkProductivityExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({WrongDateException.class})
	protected ApiErrorMessageDto handleWrongDateException(WrongDateException ex) {
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

}
