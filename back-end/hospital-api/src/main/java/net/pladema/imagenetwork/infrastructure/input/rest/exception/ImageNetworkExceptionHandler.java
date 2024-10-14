package net.pladema.imagenetwork.infrastructure.input.rest.exception;

import java.net.URISyntaxException;
import java.util.Locale;

import lombok.RequiredArgsConstructor;
import net.pladema.imagenetwork.imagequeue.application.imagemoveretry.exceptions.ImageQueueException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.exception.StudyException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "net.pladema.imagenetwork")
public class ImageNetworkExceptionHandler {

	private final MessageSource messageSource;

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({StudyException.class})
	protected ApiErrorMessageDto handleImageNetworkException(StudyException ex, Locale locale) {
		log.debug("StudyException message -> {}", ex.getMessage(), ex);
		return buildErrorMessage(ex.getCode().toString(), ex.getMessage(), locale);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({URISyntaxException.class})
	protected ApiErrorMessageDto handleImageNetworkException(URISyntaxException ex, Locale locale) {
		log.debug("URISyntaxException message -> {}", ex.getMessage(), ex);
		return buildErrorMessage(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(), locale);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ImageQueueException.class})
	protected ApiErrorMessageDto handleImageQueueException(ImageQueueException ex, Locale locale) {
		log.debug("ImageQueueException message -> {}", ex.getMessage(), ex);
		return buildErrorMessage(ex.getCode().toString(), ex.getMessage(), locale);
	}

	private ApiErrorMessageDto buildErrorMessage(String code, String message, Locale locale) {
		String errorMessage = message;
		try {
			errorMessage = messageSource.getMessage(message, null, locale);
		} catch (Exception ignored) {
			log.warn("Intentando usar message '{}'", message);
		}

		return new ApiErrorMessageDto(code, errorMessage);
	}
}
