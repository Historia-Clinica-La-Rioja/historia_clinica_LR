package ar.lamansys.sgx.auth.user.infrastructure.input.rest.exceptions;

import ar.lamansys.sgx.auth.user.application.exception.OAuthUserException;
import ar.lamansys.sgx.auth.user.application.registeruser.exceptions.RegisterUserException;
import ar.lamansys.sgx.auth.user.application.updateownpassword.exceptions.PasswordException;
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

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = {"ar.lamansys.sgx.auth"})
public class UserExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(UserExceptionHandler.class);

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ RegisterUserException.class })
	protected ApiErrorMessageDto handleRegisterUserException(RegisterUserException ex) {
		LOG.debug("RegisterUserException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ PasswordResetTokenStorageException.class })
	protected ApiErrorMessageDto handlePasswordResetTokenStorageException(PasswordResetTokenStorageException ex) {
		LOG.debug("PasswordResetTokenStorageException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ UserException.class })
	protected ApiErrorMessageDto handleUserException(UserException ex) {
		LOG.debug("UserException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ UserStorageException.class })
	protected ApiErrorMessageDto handleUserStorageException(UserStorageException ex) {
		LOG.debug("UserStorageException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ UserPasswordException.class })
	protected ApiErrorMessageDto handleUserPasswordException(UserPasswordException ex) {
		LOG.debug("UserPasswordException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ OAuthUserException.class })
	protected ApiErrorMessageDto handleOAuthUserException(OAuthUserException ex) {
		LOG.debug("OAuthUserException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ PasswordException.class })
	protected ApiErrorMessageDto handleInvalidPasswordException(PasswordException ex) {
		LOG.debug("InvalidUserException -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.code.toString(), ex.getMessage());
	}
}

