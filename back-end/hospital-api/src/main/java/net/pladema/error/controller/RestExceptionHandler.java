package net.pladema.error.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import net.pladema.error.controller.dto.ApiErrorMessage;
import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import net.pladema.error.controller.dto.ApiError;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

	private final MessageSource messageSource;

	public RestExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}


	@ExceptionHandler({ MethodArgumentNotValidException.class })
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException ex, WebRequest request) {
		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()){

			if(violation.getPropertyPath().toString().contains("<cross-parameter>"))
				errors.add(violation.getMessage());
			else
				errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
		}
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Constraint violation", errors);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public Map<String, String> handleDataIntegrityExceptions(DataIntegrityViolationException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("ERROR", ex.getCause().getCause().toString());
		LOG.error(ex.getMessage(), ex);
		return errors;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadCredentialsException.class })
	public ResponseEntity<ApiErrorMessage> invalidCredentials(BadCredentialsException ex, Locale locale) {
		LOG.warn(ex.getMessage(), ex);
		return new ResponseEntity<>(new ApiErrorMessage(ex.getMessage(), "Nombre de usuario o clave inválidos"), HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> invalidUsername(Exception ex, Locale locale) {
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, locale);
		LOG.error(errorMessage, ex);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(MessagingException.class)
	public String invalidEntity(MessagingException ex, Locale locale) {
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, locale);
		LOG.error(errorMessage, ex);
		return errorMessage;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiErrorMessage> notFound(NotFoundException ex) {
		LOG.warn(ex.getMessage(), ex);
		return new ResponseEntity<>(new ApiErrorMessage(ex.messageId, ex.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgumentExceptions(IllegalArgumentException ex, Locale locale) {
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, locale);
		LOG.error(errorMessage, ex);
		return errorMessage;
	}
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(PermissionDeniedException.class)
	public ResponseEntity<ApiErrorMessage> permissionDenied(PermissionDeniedException ex) {
		LOG.warn(ex.getMessage(), ex);
		return new ResponseEntity<>(new ApiErrorMessage("forbidden", ex.getMessage()), HttpStatus.FORBIDDEN);
	}


}