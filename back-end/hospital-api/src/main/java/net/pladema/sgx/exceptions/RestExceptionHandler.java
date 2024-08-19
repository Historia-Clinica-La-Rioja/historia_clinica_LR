package net.pladema.sgx.exceptions;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.MethodNotSupportedException;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import ar.lamansys.sgx.shared.dates.exceptions.DateParseException;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorDto;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import ar.lamansys.sgx.shared.files.exception.FileServiceEnumException;
import ar.lamansys.sgx.shared.files.exception.FileServiceException;
import ar.lamansys.sgx.shared.filestorage.application.BucketObjectAccessException;
import ar.lamansys.sgx.shared.filestorage.application.BucketObjectException;
import ar.lamansys.sgx.shared.filestorage.application.BucketObjectNotFoundException;
import ar.lamansys.sgx.shared.strings.StringValidatorException;
import net.pladema.medicalconsultation.diary.service.domain.OverturnsLimitException;
import net.pladema.sgx.healthinsurance.service.exceptions.PrivateHealthInsuranceServiceException;
import net.pladema.user.infrastructure.output.notification.exceptions.RestorePasswordNotificationException;

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
	public ApiErrorMessageDto handleDataIntegrityExceptions(DataIntegrityViolationException ex) {
		LOG.debug("DataIntegrityViolationException thrown: {}", ex.getMessage(), ex);
		Map<String, Object> errors = new HashMap<>();
		errors.put("ERROR", ex.getCause().getCause().toString());

		return new ApiErrorMessageDto(
				"data-integrity",
				ex.getMessage(),
				errors
		);
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
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, ex.getMessage(), locale);
		LOG.info(ex.getMessage());
		LOG.debug(ex.getMessage(), ex);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(MessagingException.class)
	public String invalidEntity(MessagingException ex, Locale locale) {
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, ex.getMessage(), locale);
		LOG.error("MessagingException thrown: {}", errorMessage, ex);
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
	public ApiErrorMessageDto handleIllegalArgumentExceptions(IllegalArgumentException ex) {
		return buildErrorMessage(ex);
	}
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(PermissionDeniedException.class)
	public ApiErrorMessageDto permissionDenied(PermissionDeniedException ex) {
		SecurityContextUtils.warn(ex.getMessage());
		LOG.debug(ex.getMessage(), ex);
		return new ApiErrorMessageDto("forbidden", ex.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({BackofficeValidationException.class})
	public ApiErrorMessageDto handleBackofficeValidationException(BackofficeValidationException ex, Locale locale) {
		return buildErrorMessage("bo-validation", ex.getMessage(), locale);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ValidationException.class})
	public ApiErrorMessageDto handleValidationException(ValidationException ex, Locale locale) {
		return buildErrorMessage("validation", ex.getMessage(), locale);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({OverturnsLimitException.class})
	public ApiErrorMessageDto handleOverturnsLimitException(OverturnsLimitException ex, Locale locale) {
		return buildErrorMessage("overturns-limit", ex.getMessage(), locale);
	}

	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	@ExceptionHandler({ MethodNotSupportedException.class })
	protected ResponseEntity<ApiErrorMessageDto> methodNotSupportedException(MethodNotSupportedException ex, Locale locale) {
		LOG.info(ex.getMessage());
		LOG.debug(ex.getMessage(), ex);
		return new ResponseEntity<>(new ApiErrorMessageDto(HttpStatus.NOT_IMPLEMENTED.hashCode() + "", ex.getMessage()), HttpStatus.NOT_IMPLEMENTED);
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

	@ResponseStatus(HttpStatus.INSUFFICIENT_STORAGE)
	@ExceptionHandler({ IOFileUploadException.class })
	public ApiErrorMessageDto handleIOFileUploadException(IOFileUploadException ex) {
		LOG.debug("IOFileUploadException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto("IOFileUploadException", ex.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ IOException.class })
	public ApiErrorMessageDto handleIOException(IOException ex) {
		LOG.error("IOException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto("IOException", ex.getMessage());
	}

	@ExceptionHandler(ClientAbortException.class)
	protected ResponseEntity<Object> handleClientAbortException(ClientAbortException ex, WebRequest request) {
		var requestDescription = request.getDescription(false);
	 	LOG.warn("ClientAbortException occurred: {}", requestDescription);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler({ FileServiceException.class })
	public ResponseEntity<ApiErrorMessageDto> handleFileServiceException(FileServiceException ex) {
		LOG.error("FileServiceException exception -> {}", ex.getMessage(), ex);
		var error = new ApiErrorMessageDto(ex.getCodeInfo(), ex.getMessage());
		return new ResponseEntity<>(error, FileServiceEnumException.INSUFFICIENT_STORAGE.equals(ex.getCode()) ?
				HttpStatus.INSUFFICIENT_STORAGE : HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({
			BucketObjectNotFoundException.class,
			BucketObjectAccessException.class
	})
	public ApiErrorMessageDto handleBucketObjectException(BucketObjectException boe) {
		LOG.debug("BucketObjectException {} -> {}", boe.errorCode, boe.path, boe);
		return new ApiErrorMessageDto(
				boe.errorCode,
				"No se pudo acceder al archivo"
		);
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ RestorePasswordNotificationException.class })
	protected ApiErrorMessageDto handleRestorePasswordNotificationException(RestorePasswordNotificationException ex) {
		LOG.debug("RestorePasswordNotificationException -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DateParseException.class })
	protected ApiErrorMessageDto handleDateTimeParseException(DateParseException ex) {
		LOG.debug("DateTimeParseException -> originalDateValue {}", ex.originalDateValue, ex);
		return new ApiErrorMessageDto("invalid-date", ex.originalDateValue);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ RequestRejectedException.class })
	protected ApiErrorMessageDto handleRequestRejectedException(RequestRejectedException ex) {
		LOG.warn("RequestRejectedException {}", ex.getMessage());
		return new ApiErrorMessageDto("request-rejected", ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	protected ApiErrorMessageDto handleBindException(BindException ex) {
		LOG.error("BindException -> {}", ex.getMessage());
		LOG.debug(ex.getMessage(), ex);
		String[] fieldsAndValues = ex.getBindingResult()
				.getAllErrors()
				.stream()
				.map(error -> String.format("%s=%s", ((FieldError) error).getField(),((FieldError) error).getRejectedValue()))
				.toArray(String[]::new);
		return new ApiErrorMessageDto("BindException", String.format("Error al leer el valor de %s", Arrays.toString(fieldsAndValues)));
	}



	private ApiErrorMessageDto buildErrorMessage(RuntimeException ex) {
		return new ApiErrorMessageDto(
				"RUNTIME_EXCEPTION",
				ex.getMessage()
		);
	}

	private ApiErrorMessageDto buildErrorMessage(String code, String message, Locale locale) {
		String errorMessage = message;
		try {
			errorMessage = messageSource.getMessage(message, null, locale);
		} catch (Exception ignored) {
			LOG.warn("Intentando usar message '{}'", message);
		}

		return new ApiErrorMessageDto(code, errorMessage);
	}
}