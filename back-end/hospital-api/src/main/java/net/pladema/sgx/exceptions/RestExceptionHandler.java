package net.pladema.sgx.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.MethodNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorDto;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import ar.lamansys.sgx.shared.strings.StringValidatorException;
import net.pladema.medicalconsultation.diary.service.domain.OverturnsLimitException;
import net.pladema.sgx.healthinsurance.service.exceptions.PrivateHealthInsuranceServiceException;

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
	public ApiErrorDto handleValidationExceptions(ConstraintViolationException ex, WebRequest request) {
		List<String> errors = new ArrayList<>();

		if (ex.getConstraintViolations().isEmpty()) {
			String msg = ex.getMessage();
			try {
				String property = msg.substring(0, msg.indexOf(":"));
				msg = property + ": " + messageSource.getMessage(StringUtils.substringBetween(msg, "{", "}"), null, null);
			} catch (Exception e) {
				LOG.error("No se tiene un mensaje para la siguiente clave -> {}", msg);
			}
			LOG.debug("Constraint validation error -> {}", msg);
			return new ApiErrorDto("Constraint violation", List.of(msg));
		}

		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {

			if(violation.getPropertyPath().toString().contains("<cross-parameter>"))
				errors.add(violation.getMessage());
			else
				errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
		}
		return new ApiErrorDto("Constraint violation", errors);
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
	public ApiErrorMessageDto invalidCredentials(BadCredentialsException ex) {
		LOG.warn(ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.getMessage(), "Nombre de usuario o clave inválidos");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> invalidUsername(Exception ex, Locale locale) {
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, locale);
		LOG.info(ex.getMessage());
		LOG.debug(ex.getMessage(), ex);
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
	public ApiErrorMessageDto notFound(NotFoundException ex) {
		LOG.info(ex.getMessage());
		LOG.debug(ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.messageId, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ApiErrorMessageDto handleIllegalArgumentExceptions(IllegalArgumentException ex, Locale locale) {
		return handleRuntimeException(ex, locale);
	}
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(PermissionDeniedException.class)
	public ApiErrorMessageDto permissionDenied(PermissionDeniedException ex) {
		LOG.warn(ex.getMessage(), ex);
		return new ApiErrorMessageDto("forbidden", ex.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({BackofficeValidationException.class})
	public ApiErrorMessageDto handleBackofficeValidationException(BackofficeValidationException ex, Locale locale) {
		return handleRuntimeException(ex, locale);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ValidationException.class})
	public ResponseEntity<ApiErrorMessageDto> handleValidationException(ValidationException ex, Locale locale) {
		LOG.warn(ex.getMessage(), ex);
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, locale);
		return new ResponseEntity<>(new ApiErrorMessageDto(ex.getMessage(), errorMessage), HttpStatus.BAD_REQUEST);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({OverturnsLimitException.class})
	public ApiErrorMessageDto handleOverturnsLimitException(OverturnsLimitException ex, Locale locale) {
		return handleRuntimeException(ex, locale);
	}

	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	@ExceptionHandler({ MethodNotSupportedException.class })
	protected ResponseEntity<ApiErrorMessageDto> methodNotSupportedException(MethodNotSupportedException ex, Locale locale) {
		LOG.info(ex.getMessage());
		LOG.debug(ex.getMessage(), ex);
		return new ResponseEntity<>(new ApiErrorMessageDto(HttpStatus.NOT_IMPLEMENTED.hashCode() + "", ex.getMessage()), HttpStatus.NOT_IMPLEMENTED);
	}

	private ApiErrorMessageDto handleRuntimeException(RuntimeException ex, Locale locale) {
		String errorMessage = null;
		try {
			errorMessage = messageSource.getMessage(ex.getMessage(), null, locale);
			LOG.error(errorMessage);
		}
		catch (Exception e){
			LOG.error(ex.getMessage());
		}
		return new ApiErrorMessageDto("RUNTIME_EXCEPTION",
				errorMessage != null ? errorMessage : ex.getMessage()
		);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ StringValidatorException.class })
	protected ApiErrorDto handleStringValidatorException(StringValidatorException ex) {
		LOG.info(ex.getMessage());
		LOG.debug(ex.getMessage(), ex);
		return new ApiErrorDto("String constraint violation", ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PrivateHealthInsuranceServiceException.class)
	public ApiErrorMessageDto handlePrivateHealthInsuranceServiceException(PrivateHealthInsuranceServiceException ex) {
		LOG.debug("RegisterUserException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}


}