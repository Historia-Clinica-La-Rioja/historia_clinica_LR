package net.pladema.sipplus.infrastructure.input.rest.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.sipplus")
public class SipPlusApiExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({SipPlusApiException.class})
	protected ApiErrorMessageDto handleSipPlusApiException(SipPlusApiException ex, Locale locale) {
		log.debug("SipPlusApiException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}