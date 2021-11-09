package ar.lamansys.sgx.auth.user.infrastructure.input.rest.exceptions;

import ar.lamansys.sgx.auth.user.application.registeruser.exceptions.RegisterUserException;
import ar.lamansys.sgx.auth.user.domain.passwordreset.exceptions.PasswordResetTokenStorageException;
import ar.lamansys.sgx.auth.user.domain.user.model.UserException;
import ar.lamansys.sgx.auth.user.domain.user.service.exceptions.UserStorageException;
import ar.lamansys.sgx.auth.user.domain.userpassword.UserPasswordException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = {"ar.lamansys.sgx.auth",
		"net.pladema.user.infrastructure.input.rest"})
public class UserExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(UserExceptionHandler.class);

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ RegisterUserException.class })
	protected ApiErrorMessageDto handleRegisterUserException(RegisterUserException ex, Locale locale) {
		LOG.debug("RegisterUserException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ PasswordResetTokenStorageException.class })
	protected ApiErrorMessageDto handlePasswordResetTokenStorageException(PasswordResetTokenStorageException ex, Locale locale) {
		LOG.debug("PasswordResetTokenStorageException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ UserException.class })
	protected ApiErrorMessageDto handleUserException(UserException ex, Locale locale) {
		LOG.debug("UserException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ UserStorageException.class })
	protected ApiErrorMessageDto handleUserStorageException(UserStorageException ex, Locale locale) {
		LOG.debug("UserStorageException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ UserPasswordException.class })
	protected ApiErrorMessageDto handleUserPasswordException(UserPasswordException ex, Locale locale) {
		LOG.debug("UserPasswordException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}
}

