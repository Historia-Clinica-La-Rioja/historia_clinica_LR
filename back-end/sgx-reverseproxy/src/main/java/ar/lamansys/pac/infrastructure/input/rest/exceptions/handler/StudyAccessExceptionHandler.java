package ar.lamansys.pac.infrastructure.input.rest.exceptions.handler;

import ar.lamansys.base.infrastructure.input.rest.exceptions.dto.ApiErrorMessageDto;
import ar.lamansys.pac.infrastructure.input.rest.exceptions.StudyAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys")
public class StudyAccessExceptionHandler {

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({ StudyAccessException.class })
	protected ApiErrorMessageDto handlePacException(StudyAccessException ex) {
		log.debug("PacException message -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({ HttpClientErrorException.class })
	protected ApiErrorMessageDto handleHttpClientErrorException(HttpClientErrorException ex) {
		log.debug("PacException message -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.getStatusCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ MissingServletRequestParameterException.class })
	protected ApiErrorMessageDto handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
		log.debug("MissingServletRequestParameterException message -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(HttpStatus.BAD_REQUEST.toString(), "Faltan parámetros en la URL para completar la solicitud");
	}

}
