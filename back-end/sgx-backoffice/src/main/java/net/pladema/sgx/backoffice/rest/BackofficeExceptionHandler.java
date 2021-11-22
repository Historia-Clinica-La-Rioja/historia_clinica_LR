package net.pladema.sgx.backoffice.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.pladema.sgx.backoffice.exceptions.PermissionDeniedException;
import net.pladema.sgx.error.controller.dto.ApiErrorMessage;

@RestControllerAdvice
public class BackofficeExceptionHandler {

	private final Logger logger;

	public BackofficeExceptionHandler() {
		this.logger = LoggerFactory.getLogger(getClass());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({PermissionDeniedException.class})
	public ApiErrorMessage handlePermissionDeniedException(PermissionDeniedException ex) {
		logger.warn(ex.getMessage());
		logger.debug(ex.getMessage(), ex);
		return new ApiErrorMessage("forbidden", ex.getMessage());
	}
}
